.PHONY: help

## print help text
help:
	@echo '===================================================================='
	@echo '  Before running any target with this Makefile, a config.env'
	@echo '  needs to be placed in the same directory as'
	@echo '  this file. config.env needs a few variables defined:'
	@echo ''
	@echo '    USE_DOCKER={boolean}   # start the services in containers? If false, all services will be started using their jar files'
	@echo '    SPRING_PROFILES_ACTIVE={string} 	# comma seperated list of active profiles. NOTE: do not run "local" and "cloud" together. It will cause issues'
	@echo ''
	@echo '    CONFIG_GIT_URI={string}    # path to config git repository. Only needed when spring profile is set to "cloud"'
	@echo '    CONFIG_GIT_USER={string}    # user to log into the config git repo'
	@echo '    CONFIG_GIT_PW={string}    # password to log into the config git repo'
	@echo ''
	@echo '    VAULT_URI={string}	# hashicorp vault uri'
	@echo '    VAULT_TOKEN={string}	# vault authentication token'
	@echo '    VAULT_SCHEME={string}	# either http or https'
	@echo ''
	@echo '    APP_PORT={number}   # the port number to host the appplication'
	@echo ''
	@echo '===================================================================='
	@echo 'Makefile options:'
	@echo ''
	@echo '  help:' 
	@echo '    Prints this help text'
	@echo ''
	@echo '  build:'
	@echo '    Starts a build of all services'
	@echo '    If wanting to specify specific services to build,'
	@echo '    pass in the SERVICES= argument.'
	@echo ''
	@echo '    Examples:' 
	@echo '	     make SERVICES="itb-diapers" build'
	@echo ''
	@echo '  start:' 
	@echo '    Starts the cloud services in'
	@echo '    the proper order and makes them'
	@echo '    available.'
	@echo ''
	@echo '    Examples:'
	@echo '      make start'
	@echo ''
	@echo '  add:'
	@echo '    Add a service to the existing cluster. If that service is already'
	@echo '    deployed to the cluster, then it will simply add another app for load balancing.'
	@echo '    Note that "make start" needs to have been called first.'
	@echo ''
	@echo '	   Examples:'
	@echo '      make SERVICES="itb-diapers" add'
	@echo ''
	@echo '  drop:'
	@echo '    Drop a service from the existing cluster. If the service is not deployed to the cluster'
	@echo '    it wont do anything. Also, if there are multple instances of the same service, it will'
	@echo '    scale down the instances by 1.'
	@echo ''
	@echo '	   Examples:'
	@echo '      make SERVICES="itb-diapers" drop'
	@echo ''
	@echo '  refresh:'
	@echo '    Kills any running proccess for the service and '
	@echo '    rebuilds/redeploys that service back into the'
	@echo '    cluster'
	@echo ''
	@echo '    Examples:'
	@echo '      make SERVICES="itb-auth" refresh'
	@echo '      make SERVICES="itb-diapers itb-sleep itb-feedings"'
	@echo ''
	@echo '  status:'
	@echo '    Provides a health status for all running services'
	@echo ''
	@echo '  stop:'
	@echo '    Stopds all running services.'
	
	
## check config before all
testconfig:
	@test -f config.env || echo 'config.env is required and cannot be found. Use "make help" for example'
	@test -f config.env || exit 1
	
## build all services
buildsrc:
	@usage.sh build $(SERVICES)
build: testconfig buildsrc
	
	
## start all services
startsrc:
	@usage.sh start $(SERVICES)
start: testconfig startsrc


## Stop all processes
stop:
	@usage.sh stop
	
	
## refresh certain processes
refreshsrc:
	@usage.sh refresh $(SERVICES)
refresh: testconfig refreshsrc