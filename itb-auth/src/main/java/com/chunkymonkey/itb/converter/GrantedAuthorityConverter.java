package com.chunkymonkey.itb.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

public class GrantedAuthorityConverter {

	@Component
	public static class GrantedAuthorityFromConverter implements Converter<GrantedAuthority, String> {

		@Override
		public String convert(GrantedAuthority source) {
			return source == null ? null : source.getAuthority();
		}
		
	}
	
	@Component
	public static class GrantedAuthorityToConverter implements Converter<String, GrantedAuthority> {

		@Override
		public GrantedAuthority convert(String source) {
			return source == null ? null : new SimpleGrantedAuthority(source);
		}
		
	}
	
}
