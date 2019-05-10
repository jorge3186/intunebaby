package com.chunkymonkey.itb.validation;

import javax.validation.ValidationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItbValidatorImpl implements ItbValidator {

	private final static Logger logger = LoggerFactory.getLogger(ItbValidatorImpl.class);
	
	private final Validator validator;
	
	@Autowired
	public ItbValidatorImpl(Validator validator) {
		this.validator = validator;
	}
	
	@Override
	public <T> void validate(T obj) {
		if (obj != null) {
			validateObject(obj);
		}
	}

	private <T> void validateObject(T user) {
		var violations = validator.validate(user);
		
		if (!violations.isEmpty()) {
			violations.forEach(v -> logger.error(v.getMessage()));
			throw new ValidationException(violations.stream()
					.map(v -> {
						StringBuilder b = new StringBuilder();
						b.append(v.getRootBean().getClass().getSimpleName() + ".");
						b.append(v.getPropertyPath().toString() + " ");
						b.append(v.getMessage());
						b.append(System.lineSeparator());
						return b.toString();
					})
					.reduce("", (a, b) -> a + b));
		}
	}
	
}
