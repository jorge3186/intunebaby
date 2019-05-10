package com.chunkymonkey.itb.config;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurer {

	@Bean
	public Validator beanValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}
	
}
