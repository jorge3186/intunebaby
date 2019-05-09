package com.chunkymonkey.itb.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.chunkymonkey.itb.repository.BabyRepository;

public class BabyServiceTest {

	private BabyRepository repo;
	
	private BabyService tested;
	
	@Before
	public void setup() {
		repo = Mockito.mock(BabyRepository.class);
		tested = new BabyServiceImpl(repo);
		
		//Mockito.doReturn(toBeReturned)
	}
	
	@Test
	public void testGetBabiesByUser() {
		
	}
}
