package com.chunkymonkey.itb.graphql.domain;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class ItbAuthentication {

	private Object principal;
	
	private Object details;
	
	private String name;
	
	private Object credentials;
	
	public ItbAuthentication(OAuth2Authentication oauth) {
		principal = oauth.getPrincipal();
		details = oauth.getDetails();
		name = oauth.getName();
		credentials = oauth.getCredentials();
	}

	public Object getPrincipal() {
		return principal;
	}

	public void setPrincipal(Object principal) {
		this.principal = principal;
	}

	public Object getDetails() {
		return details;
	}

	public void setDetails(Object details) {
		this.details = details;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getCredentials() {
		return credentials;
	}

	public void setCredentials(Object credentials) {
		this.credentials = credentials;
	}
	
}
