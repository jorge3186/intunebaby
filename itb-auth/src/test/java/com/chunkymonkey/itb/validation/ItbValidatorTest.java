package com.chunkymonkey.itb.validation;

import java.util.Collections;

import javax.validation.Validation;
import javax.validation.ValidationException;

import org.junit.Before;
import org.junit.Test;

import com.chunkymonkey.itb.domain.ItbUser;

public class ItbValidatorTest {
	
	private ItbValidator tested;
	
	@Before
	public void setup() {
		tested = new ItbValidatorImpl(Validation.buildDefaultValidatorFactory().getValidator());
	}
	
	@Test
	public void testNullObject() {
		tested.validate(null); //does nothing
	}
	
	@Test(expected = ValidationException.class)
	public void testInvalidObject() {
		var user = new ItbUser();
		user.setUsername("a user");
		tested.validate(user);
	}
	
	@Test
	public void testValidObject() {
		var user = new ItbUser();
		user.setUsername("a user");
		user.setPassword("a password");
		user.setEmail("aemail@mail.com");
		user.setAuthorities(Collections.emptyList());
		tested.validate(user);
	}

}
