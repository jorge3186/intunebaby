package com.chunkymonkey.itb.filter;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sun.jersey.api.client.ClientRequest;

public class DiscoveryClientFilterTest {

	private DiscoveryClientFilter tested;
	
	private ClientRequest request;
	
	private MultivaluedMap<String, Object> headers;
	
	private Map<String, String> actualHeaders = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		request = Mockito.mock(ClientRequest.class);
		headers = Mockito.mock(MultivaluedMap.class);
		
		Mockito.doReturn(headers).when(request).getHeaders();
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				actualHeaders.put(invocation.getArgument(0), invocation.getArgument(1));
				return null;
			}
		}).when(headers).add(Mockito.anyString(), Mockito.any());
	}
	
	@Test
	public void testClientCredentials() {
		Assert.assertTrue(Integer.valueOf(0).equals(actualHeaders.size()));
		tested = new DiscoveryClientFilter("user", "password");
		tested.handle(request);
		Assert.assertTrue(Integer.valueOf(1).equals(actualHeaders.size()));
		Assert.assertTrue("Basic dXNlcjpwYXNzd29yZA==".equals(actualHeaders.get("Authorization")));
	}
}
