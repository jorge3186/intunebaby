package com.chunkymonkey.itb;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.chunkymonkey.itb.config.MockOauth2TokenGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = WebEnvironment.RANDOM_PORT,
		classes = {ItbBabyServer.class, MockOauth2TokenGenerator.class}, 
		properties = {
				"spring.cloud.vault.enabled=false", 
				"spring.cloud.config.enabled=false",
				"eureka.client.enabled=false"})
@ActiveProfiles({"test"})
public class ItbBabyServerTest {

	@LocalServerPort
	private int port;
	
	@Autowired
    private TestRestTemplate restTemplate;
    
	@Autowired
	private MockOauth2TokenGenerator tokenGenerator;
	
	@Test
	public void contextLoaded() throws Exception {}
	
	@Test
	public void testActuatorEndpoints() {
		// allow all access to /actuator/health
		ResponseEntity<String> resp = this.restTemplate.getForEntity("http://localhost:" + port + "/actuator/health" , String.class);
		Assert.assertTrue(HttpStatus.OK.equals(resp.getStatusCode()));
		Assert.assertTrue(resp.getBody().contains("UP"));
		
		// deny access to /actuator/**
		resp = this.restTemplate.getForEntity("http://localhost:" + port + "/actuator/beans" , String.class);
		Assert.assertTrue(HttpStatus.UNAUTHORIZED.equals(resp.getStatusCode()));
		Assert.assertTrue(resp.getBody().contains("Full authentication is required to access this resource"));
		
		
		// access to /actuator/** with client_credentials token auth (scope = 'server')
		OAuth2AccessToken token = tokenGenerator.generateClientToken("itb-baby", getAuthorities());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token.getValue());
		HttpEntity<String> request = new HttpEntity<String>(headers);
		resp = this.restTemplate.exchange("http://localhost:" + port + "/actuator/beans", HttpMethod.GET, 
				request, String.class);
		Assert.assertTrue(HttpStatus.OK.equals(resp.getStatusCode()));
		Assert.assertTrue(resp.getBody().contains("beans"));
		
		// deny to /actuator/** with ui token auth (scope = 'ui')
		token = tokenGenerator.generateUiToken("user", "password", getAuthorities());
		headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token.getValue());
		request = new HttpEntity<String>(headers);
		resp = this.restTemplate.exchange("http://localhost:" + port + "/actuator/beans", HttpMethod.GET, 
				request, String.class);
		Assert.assertTrue(HttpStatus.FORBIDDEN.equals(resp.getStatusCode()));
		Assert.assertTrue(resp.getBody().contains("Insufficient scope for this resource"));
	}
	
	private List<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<>();
		auths.add(new SimpleGrantedAuthority("ROLE_USER"));
		auths.add(new SimpleGrantedAuthority("ROLE_CONFIGUSER"));
		return auths;
	}
	
}
