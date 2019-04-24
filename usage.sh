#!/bin/sh

# color codes
RED="\e[31m"
GREEN="\e[32m"
YELLOW="\e[33m"
BLUE="\e[34m"
PURPLE="\e[35m"
CE="\e[0m"

# export the configuration file
export $(grep -v '^#' config.env | xargs -0)

# get OS
unameOut="$(uname -s)"
case "${unameOut}" in
	CYGWIN*) 	MACHINE=WIN;;
	LINUX*)		MACHINE=LINUX;;
	*) 			MACHINE=UNKNOWN
esac

# get kill proc based on OS
if [ "$MACHINE" == "WIN" ];
then
	KILL_PROC="taskkill /F /PID"
else
	KILL_PROC="kill -9"
fi


###########################################################
# update env
function update_env() {
	local config_resp=$(curl --header "X-Vault-Token: $VAULT_TOKEN" --request GET  "$VAULT_URI/v1/secret/data/itb-generic")
	export CONFIG_USER=$(echo $config_resp | jq '.data.data."itb.config-username"' | tr -d '"')
	export CONFIG_PW=$(echo $config_resp | jq '.data.data."itb.config-password"' | tr -d '"')
}

###########################################################
# build a service func
function build_service() {
	echo -e "$CS$GREEN-------------------------------------------$CE"
	echo -e "$CS$GREEN--$CE $CS$YELLOWbuilding $1$CE"
	gradle $1:clean $1:bootJar --info
	echo -e "$CS$GREEN--$CE $CS$YELLOW$1 build complete$CE"
	echo -e "$CS$GREEN-------------------------------------------$CE"
}

###########################################################
# start a service func
function start_service() {
	IS_UP="{\"status\":\"UP\"}"
	
	echo -e "$GREEN -------------------------------------------$CE"
	echo -e "$GREEN -- $CE $YELLOW starting $2$CE"
	local jarname=$(ls $2/build/libs | grep jar)
	java -jar ./$2/build/libs/$jarname --info &
	
	local pid=
	while [ "$pid" == "" ]
	do
		sleep 5
		local pid=$(jcmd | grep $jarname | awk '{print $1}')
	done
	
	local port=
	while [ "$port" == "" ]
	do
		sleep 5
		local port=$(netstat -a -n -o | grep $pid | grep '[::]:' | awk '{print $2}' | sed -En s/\\[::\\]://p)
	done
	
	if [ "$2" == "itb-config" ];
	then
		export CONFIG_URI=http://localhost:$port
	fi
	if [ "$2" == "itb-discovery" ];
	then
		export DISCOVERY_URI=http://localhost:$port/eureka
	fi
	if [ "$2" == "itb-auth" ];
	then
		export AUTH_URI=http://localhost:$port
	fi
	
	resp=$(wget --tries 50 --retry-connrefused "$1:$port/actuator/health" -q -O -)
	echo -e ''
	if [ "$resp" != $IS_UP ]
	then
		echo -e "$RED -- $CE $RED FATAL: $CE $2 failed to start"
		echo -e "$RED -- $CE health check return: $resp"
		echo -e "$RED -- $CE $RED FATAL: $CE Check make.log file for errors"
		exit 100
	fi
	echo -e "$CS$GREEN -- $CE health check for $2 passed: $resp"
	echo -e "$CS$GREEN -- $CE $2 running at $1:$port"
	echo -e "$CS$GREEN ------------------------------------------- $CE"
}


###########################################################
# stop a service func
function stop_service() {
	local pid=$(jcmd | grep $1 | awk '{print $1}')
	if [ "$pid" != "" ];
	then
		$KILL_PROC $pid
		echo -e "$CS$GREEN $1 has been stopped $CE"
	fi
}


###########################################################
# prcoesses


# start or refresh
PROC=$1


# run build
if [ "$PROC" == "build" ];
then
	if [ "$#" -eq 1 ];
	then
		build_service 'itb-config'
		build_service 'itb-auth'
	else
		for arg in ${@:2}
		do 
			build_service $arg
		done
	fi
fi

# run start
if [ "$PROC" == "start" ];
then
	echo -e "$YELLOW starting itb-cloud services $CE"
	echo -e "$YELLOW ================================================ $CE"
	
	echo -e ''
	start_service http://localhost 'itb-config'
	update_env
	start_service http://localhost 'itb-auth'
fi

# run stop
if [ "$PROC" == "stop" ];
then
	echo -e 'Stopping itb-cloud services'
	pids=$(jcmd | grep itb- | awk '{print $1}')
	for pid in $pids
	do
		$KILL_PROC $pid
	done
	echo -e "$CS$YELLOW ================================================ $CE"
	echo -e "$CS$GREEN itb-cloud has been successfully stopped $CE"
fi


