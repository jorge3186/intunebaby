package com.chunkymonkey.itb.config;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;

import com.chunkymonkey.itb.converter.GrantedAuthorityDeserializer;
import com.chunkymonkey.itb.converter.GrantedAuthoritySerializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class BeanConfigurer {
	
	@Bean
	public Validator beanValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@Bean @Primary
	public ObjectMapper jacksonMapper() {
		var mapper = new ObjectMapper();
		
		mapper.registerModule(mapSerializerModule(
				new GrantedAuthorityDeserializer(), new GrantedAuthoritySerializer(), 
				GrantedAuthority.class));
		
		return mapper;
	}
	
	private <D extends JsonDeserializer<T>, S extends JsonSerializer<T>,  T> 
		SimpleModule mapSerializerModule(D deserializer, S serializer,  Class<T> type) {
		var m = new SimpleModule();
		m.addDeserializer(type, deserializer);
		m.addSerializer(type, serializer);
		return m;
	}
}
