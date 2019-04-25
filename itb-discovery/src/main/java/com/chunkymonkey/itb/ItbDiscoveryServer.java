package com.chunkymonkey.itb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ItbDiscoveryServer {

	public static void main(String[] args) {
		SpringApplication.run(ItbDiscoveryServer.class, args);
	}
	
}
