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
        var approved = true;
        String redirectUrl = null;
        Set<String> responseTypes = Collections.emptySet();
        Map<String, Serializable> extensionProperties = Collections.emptyMap();
        var scopes = new HashSet<String>();
        scopes.add("server");
        var resourceIds = new HashSet<String>();
        resourceIds.add("itb-baby");
		 
		var req = new OAuth2Request(requestParameters, clientId, authorities, approved, scopes,
                resourceIds, redirectUrl, responseTypes, extensionProperties);
		var princ = new User(clientId, "", true, true, true, true, authorities);
		var authenticationToken = new UsernamePasswordAuthenticationToken(princ, null, authorities);
        var auth = new OAuth2Authentication(req, authenticationToken);
        return tokenServices().createAccessToken(auth);
	}
	
	public OAuth2AccessToken generateUiToken(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		Map<String, String> requestParameters = Collections.emptyMap();
        var approved = true;
        String redirectUrl = null;
        Set<String> responseTypes = Collections.emptySet();
        Map<String, Serializable> extensionProperties = Collections.emptyMap();
        var scopes = new HashSet<String>();
        scopes.add("ui");
        var resourceIds = new HashSet<String>();
        resourceIds.add("itb-baby");
		 
		var req = new OAuth2Request(requestParameters, username, authorities, approved, scopes,
                resourceIds, redirectUrl, responseTypes, extensionProperties);
		var princ = new User(username, password, true, true, true, true, authorities);
		var authenticationToken = new UsernamePasswordAuthenticationToken(princ, null, authorities);
        var auth = new OAuth2Authentication(req, authenticationToken);
        return tokenServices().createAccessToken(auth);
	}
	
	public TokenStore jwtTokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	public JwtAccessTokenConverter accessTokenConverter() {
		var converter = new JwtAccessTokenConverter();
		converter.setVerifierKey(env.getProperty("itb.verifier-key"));
		converter.setSigningKey(env.getProperty("itb.signing-key"));
		return converter;
	}

	public DefaultTokenServices tokenServices() {
		var services = new DefaultTokenServices();
		services.setTokenStore(jwtTokenStore());
		services.setTokenEnhancer(accessTokenConverter());
		services.setSupportRefreshToken(true);
		return services;
	}
	
}
