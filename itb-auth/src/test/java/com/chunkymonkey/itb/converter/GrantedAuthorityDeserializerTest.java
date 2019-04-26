package com.chunkymonkey.itb.converter;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

public class GrantedAuthorityDeserializerTest {

	private GrantedAuthorityDeserializer tested;
	
	private DeserializationContext ctx;
	
	private JsonParser parser;
	
	@Before
	public void setup() {
		ctx = Mockito.mock(DeserializationContext.class);
		parser = Mockito.mock(JsonParser.class);
		tested = new GrantedAuthorityDeserializer();
	}
	
	@Test
	public void testDeserializeNull() throws JsonProcessingException, IOException {
		Mockito.when(parser.getValueAsString()).thenReturn("");
		GrantedAuthority authority = tested.deserialize(parser, ctx);
		Assert.assertTrue(authority == null);
		
		Mockito.when(parser.getValueAsString()).thenReturn(null);
		authority = tested.deserialize(parser, ctx);
		Assert.assertTrue(authority == null);
	}
	
	@Test
	public void testValidParse() throws JsonProcessingException, IOException {
		Mockito.when(parser.getValueAsString()).thenReturn("ROLE_USER");
		GrantedAuthority authority = tested.deserialize(parser, ctx);
		Assert.assertTrue("ROLE_USER".equals(authority.getAuthority()));
	}
}
