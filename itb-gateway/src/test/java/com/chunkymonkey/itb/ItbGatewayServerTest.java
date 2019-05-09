package com.chunkymonkey.itb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = {ItbGatewayServer.class}, 
		properties = {
				"spring.cloud.vault.enabled=false",
				"spring.cloud.config.enabled=false",
				"eureka.client.enabled=false"})
@ActiveProfiles({"test"})
public class ItbGatewayServerTest {

	@LocalServerPort
	private int port;
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	@Test
    public void contextLoads() throws Exception {}
	
	@Test
	public void testActuatorEndpoints() {
		// allow all access to /actuator/health
		var resp = this.restTemplate.getForEntity("http://localhost:" + port + "/actuator/health" , String.class);
		Assert.assertTrue(HttpStatus.OK.equals(resp.getStatusCode()));
		Assert.assertTrue(resp.getBody().contains("UP"));
		
		// deny access to /actuator/**
		resp = this.restTemplate.getForEntity("http://localhost:" + port + "/actuator/beans" , String.class);
		Assert.assertTrue(HttpStatus.UNAUTHORIZED.equals(resp.getStatusCode()));
		Assert.assertTrue(resp.getBody().contains("Unauthorized"));
		
		// allow access to /actuator/** with valid basic authentication
		resp = this.restTemplate.withBasicAuth("user", "password").getForEntity("http://localhost:" + port + "/actuator/beans" , String.class);
		Assert.assertTrue(HttpStatus.OK.equals(resp.getStatusCode()));
		Assert.assertTrue(resp.getBody().contains("beans"));
	}
	
}
