package com.chunkymonkey.itb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ItbConfigServer {

	public static void main(String[] args) {
		SpringApplication.run(ItbConfigServer.class, args);
	}
	
}