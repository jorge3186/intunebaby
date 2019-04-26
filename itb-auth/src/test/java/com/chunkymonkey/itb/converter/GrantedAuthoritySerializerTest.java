package com.chunkymonkey.itb.converter;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GrantedAuthoritySerializerTest {

	private GrantedAuthoritySerializer tested;
	
	private JsonGenerator gen;
	
	private SerializerProvider provider;
	
	@Before
	public void setup() {
		tested = new GrantedAuthoritySerializer();
		gen = Mockito.mock(JsonGenerator.class);
		provider = Mockito.mock(SerializerProvider.class);
	}
	
	@Test
	public void testSerializeNull() throws IOException {
		tested.serialize(null, gen, provider);
		Mockito.verify(gen, Mockito.times(1)).writeNull();
	}
	
	@Test
	public void testSerializeValue() throws IOException {
		tested.serialize(new SimpleGrantedAuthority("ROLE_SOMETHING"), gen, provider);
		Mockito.verify(gen, Mockito.times(1)).writeString("ROLE_SOMETHING");
	}
}
