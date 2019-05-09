package com.chunkymonkey.itb.validation;

import java.time.LocalDateTime;

import javax.validation.Validation;
import javax.validation.ValidationException;

import org.junit.Before;
import org.junit.Test;

import com.chunkymonkey.itb.domain.BabyEntity;
import com.chunkymonkey.itb.domain.Gender;

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
		var baby = new BabyEntity();
		baby.setFirstName("a firstname");
		tested.validate(baby);
	}
	
	@Test
	public void testValidObject() {
		var baby = new BabyEntity();
		baby.setFirstName("a firstname");
		baby.setAccountName("an account");
		baby.setLastName("a lastname");
		baby.setBirthday(LocalDateTime.now().minusDays(50L));
		baby.setGender(Gender.MALE);
		tested.validate(baby);
	}

}
