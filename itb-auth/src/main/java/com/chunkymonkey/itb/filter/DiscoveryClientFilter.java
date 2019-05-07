package com.chunkymonkey.itb.filter;

import org.springframework.util.Base64Utils;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

public class DiscoveryClientFilter extends ClientFilter {
	
	private final String username;
	
	private final String password;
	
	public DiscoveryClientFilter(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
		String auth = username + ":" + password;
		String encoded = Base64Utils.encodeToString(auth.getBytes());
		cr.getHeaders().add("Authorization", String.format("Basic %s", encoded));
		
		return getNext() == null ? null : getNext().handle(cr);
	}

}
