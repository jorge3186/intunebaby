package com.chunkymonkey.itb.service;

import javax.validation.Validation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.chunkymonkey.itb.repository.BabyRepository;
import com.chunkymonkey.itb.validation.ItbValidator;
import com.chunkymonkey.itb.validation.ItbValidatorImpl;

public class BabyServiceTest {

	private BabyRepository repo;
	
	private BabyService tested;
	
	private ItbValidator validator;
	
	@Before
	public void setup() {
		repo = Mockito.mock(BabyRepository.class);
		validator = new ItbValidatorImpl(Validation.buildDefaultValidatorFactory().getValidator());
		tested = new BabyServiceImpl(repo, validator);
		
		//Mockito.doReturn(toBeReturned)
	}
	
	@Test
	public void testGetBabiesByUserValid() {
		tested.getBabiesForUser("existing-user");
	}
}
