package com.chunkymonkey.itb.validation;

import org.springframework.stereotype.Component;

@Component
public interface ItbValidator {
	
	<T> void validate(T obj);

}
