package com.chunkymonkey.itb.validation;

import org.springframework.stereotype.Component;

public interface ItbValidator {
	
	<T> void validate(T obj);

}
