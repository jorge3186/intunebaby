package com.chunkymonkey.itb.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@TestComponent
public class MockOauth2TokenGenerator {

	@Autowired
	private Environment env;
	
	public OAuth2AccessToken generateClientToken(String clientId, Collection<? extends GrantedAuthority> authorities) {
		Map<String, String> requestParameters = Collections.emptyMap();
        boolean approved = true;
        String redirectUrl = null;
        Set<String> responseTypes = Collections.emptySet();
        Map<String, Serializable> extensionProperties = Collections.emptyMap();
        Set<String> scopes = new HashSet<>();
        scopes.add("server");
        Set<String> resourceIds = new HashSet<>();
        resourceIds.add("itb-baby");
		 
		OAuth2Request req = new OAuth2Request(requestParameters, clientId, authorities, approved, scopes,
                resourceIds, redirectUrl, responseTypes, extensionProperties);
		User princ = new User(clientId, "", true, true, true, true, authorities);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(princ, null, authorities);
        OAuth2Authentication auth = new OAuth2Authentication(req, authenticationToken);
        return tokenServices().createAccessToken(auth);
	}
	
	public OAuth2AccessToken generateUiToken(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		Map<String, String> requestParameters = Collections.emptyMap();
        boolean approved = true;
        String redirectUrl = null;
        Set<String> responseTypes = Collections.emptySet();
        Map<String, Serializable> extensionProperties = Collections.emptyMap();
        Set<String> scopes = new HashSet<>();
        scopes.add("ui");
        Set<String> resourceIds = new HashSet<>();
        resourceIds.add("itb-baby");
		 
		OAuth2Request req = new OAuth2Request(requestParameters, username, authorities, approved, scopes,
                resourceIds, redirectUrl, responseTypes, extensionProperties);
		User princ = new User(username, password, true, true, true, true, authorities);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(princ, null, authorities);
        OAuth2Authentication auth = new OAuth2Authentication(req, authenticationToken);
        return tokenServices().createAccessToken(auth);
	}
	
	public TokenStore jwtTokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setVerifierKey(env.getProperty("itb.verifier-key"));
		converter.setSigningKey(env.getProperty("itb.signing-key"));
		return converter;
	}

	public DefaultTokenServices tokenServices() {
		DefaultTokenServices services = new DefaultTokenServices();
		services.setTokenStore(jwtTokenStore());
		services.setTokenEnhancer(accessTokenConverter());
		services.setSupportRefreshToken(true);
		return services;
	}
	
}
