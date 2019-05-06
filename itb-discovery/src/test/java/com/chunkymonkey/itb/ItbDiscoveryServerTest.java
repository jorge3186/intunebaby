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
		classes = {ItbDiscoveryServer.class}, 
		properties = {
				"spring.cloud.vault.enabled=false",
				"spring.cloud.config.enabled=false"})
@ActiveProfiles({"test"})
public class ItbDiscoveryServerTest {

	@LocalServerPort
	private int port;
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	@Test
    public void contextLoads() throws Exception {}
	
	@Test
	public void testActuatorEndpoints() {
		// allow all access to /actuator/health
		ResponseEntity<String> resp = this.restTemplate.getForEntity("http://localhost:" + port + "/actuator/health" , String.class);
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
	
	@Test
	public void testEurekaEndpoints() {
		// deny access to eureka UI
		ResponseEntity<String> resp = this.restTemplate.getForEntity("http://localhost:" + port + "/eureka" , String.class);
		Assert.assertTrue(HttpStatus.UNAUTHORIZED.equals(resp.getStatusCode()));
		
		// access allowed with basic auth
		resp = this.restTemplate.withBasicAuth("user", "password").getForEntity("http://localhost:" + port + "/eureka" , String.class);
		Assert.assertTrue(!HttpStatus.UNAUTHORIZED.equals(resp.getStatusCode()));
		
		// deny access to eureka REST operations
		resp = this.restTemplate.getForEntity("http://localhost:" + port + "/eureka/v2/apps" , String.class);
		Assert.assertTrue(HttpStatus.UNAUTHORIZED.equals(resp.getStatusCode()));
		
		// access allowed with basic auth
		resp = this.restTemplate.withBasicAuth("user", "password").getForEntity("http://localhost:" + port + "/eureka/v2/apps" , String.class);
		Assert.assertTrue(!HttpStatus.UNAUTHORIZED.equals(resp.getStatusCode()));
	}
}
