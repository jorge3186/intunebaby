package com.chunkymonkey.itb.config;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chunkymonkey.itb.validation.ItbValidator;
import com.chunkymonkey.itb.validation.ItbValidatorImpl;

@Configuration
public class BeanConfigurer {

	@Bean
	public Validator beanValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Bean
	public ItbValidator validator() {
		return new ItbValidatorImpl(beanValidator());
	}
	
}
