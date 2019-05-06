package com.chunkymonkey.itb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.annotation.JsonProperty;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = {ItbAuthServer.class}, 
		properties = {
				"spring.cloud.vault.enabled=false", 
				"spring.cloud.config.enabled=false",
				"eureka.client.enabled=false"})
@ActiveProfiles({"test"})
public class ItbAuthServerTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired
    private TestRestTemplate restTemplate;
	
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
		
		// get access token from oauth2
		ResponseEntity<OAuth2TokenResponse> tokenResp = this.restTemplate
				.withBasicAuth("itb-baby", "password")
				.postForEntity("http://localhost:" + port + "/oauth/token?grant_type=client_credentials",  new OAuth2Request(), OAuth2TokenResponse.class);
		Assert.assertTrue(HttpStatus.OK.equals(tokenResp.getStatusCode()));
		
		// access to /actuator/** with token auth
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + tokenResp.getBody().getAccessToken());
		HttpEntity<String> request = new HttpEntity<String>(headers);
		resp = this.restTemplate.exchange("http://localhost:" + port + "/actuator/beans", HttpMethod.GET, 
				request, String.class);
		Assert.assertTrue(HttpStatus.OK.equals(resp.getStatusCode()));
		Assert.assertTrue(resp.getBody().contains("beans"));
	}
	
	@Test
	public void testOAuth2() {
		ResponseEntity<OAuth2TokenResponse> tokenResp = this.restTemplate
				.withBasicAuth("itb-baby", "badpassword")
				.postForEntity("http://localhost:" + port + "/oauth/token?grant_type=client_credentials",  new OAuth2Request(), OAuth2TokenResponse.class);
		Assert.assertTrue(HttpStatus.UNAUTHORIZED.equals(tokenResp.getStatusCode()));
		
		// get access token from oauth2
		tokenResp = this.restTemplate
				.withBasicAuth("itb-baby", "password")
				.postForEntity("http://localhost:" + port + "/oauth/token?grant_type=client_credentials",  new OAuth2Request(), OAuth2TokenResponse.class);
		Assert.assertTrue(HttpStatus.OK.equals(tokenResp.getStatusCode()));
	}
	
	public static class OAuth2TokenResponse {
		
		@JsonProperty("access_token")
		private String accessToken;
		
		@JsonProperty("token_type")
		private String tokenType;
		
		@JsonProperty("expires_in")
		private Integer expiresIn;
		
		@JsonProperty("refresh_token")
		private String refreshToken;
		
		private String timestamp;
		
		private String status;
		
		private String error;
		
		@JsonProperty("error_description")
		private String errorDescription;
		
		private String message;
		
		private String path;
		
		private String jti;
		
		private String scope;

		public String getAccessToken() {
			return accessToken;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

		public String getTokenType() {
			return tokenType;
		}

		public void setTokenType(String tokenType) {
			this.tokenType = tokenType;
		}

		public Integer getExpiresIn() {
			return expiresIn;
		}

		public void setExpiresIn(Integer expiresIn) {
			this.expiresIn = expiresIn;
		}

		public String getRefreshToken() {
			return refreshToken;
		}

		public void setRefreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getJti() {
			return jti;
		}

		public void setJti(String jti) {
			this.jti = jti;
		}

		public String getScope() {
			return scope;
		}

		public void setScope(String scope) {
			this.scope = scope;
		}

		public String getErrorDescription() {
			return errorDescription;
		}

		public void setErrorDescription(String errorDescription) {
			this.errorDescription = errorDescription;
		}
		
	}
	
	public static class OAuth2Request {
		
		@JsonProperty("grant_type")
		private String grantType;
		
		@JsonProperty("client_id")
		private String clientId;
		
		@JsonProperty("client_secret")
		private String clientSecret;

		public String getGrantType() {
			return grantType;
		}

		public void setGrantType(String grantType) {
			this.grantType = grantType;
		}

		public String getClientId() {
			return clientId;
		}

		public void setClientId(String clientId) {
			this.clientId = clientId;
		}

		public String getClientSecret() {
			return clientSecret;
		}

		public void setClientSecret(String clientSecret) {
			this.clientSecret = clientSecret;
		}
	}
}
