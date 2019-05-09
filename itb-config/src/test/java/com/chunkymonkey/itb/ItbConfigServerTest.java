package com.chunkymonkey.itb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.chunkymonkey.itb.config.SecurityConfigurer;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = {ItbConfigServer.class, SecurityConfigurer.class}, 
		properties = {"spring.cloud.vault.enabled=false"})
@ActiveProfiles({"test", "local", "native"})
public class ItbConfigServerTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	@Test
    public void contextLoads() throws Exception {}
	
	@Test
	public void testConfigCallSecurity() {
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
	
	@Test
	public void testGetConfigItems() {
		var resp = this.restTemplate.getForEntity("http://localhost:" + port + "/application", String.class);
		Assert.assertTrue(HttpStatus.UNAUTHORIZED.equals(resp.getStatusCode()));
		
		resp = this.restTemplate.withBasicAuth("user", "password").getForEntity("http://localhost:" + port + "/myclient/local", String.class);
		Assert.assertTrue(HttpStatus.OK.equals(resp.getStatusCode()));
		Assert.assertTrue(resp.getBody().contains("{\"some.property\":\"some-value\",\"another.property\":\"another-value\"}"));
		Assert.assertTrue(resp.getBody().contains("{\"local.property\":\"local-value\"}"));
	}

}
