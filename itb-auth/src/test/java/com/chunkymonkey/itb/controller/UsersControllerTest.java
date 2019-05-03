package com.chunkymonkey.itb.controller;

import org.junit.Before;
import org.mockito.Mockito;

import com.chunkymonkey.itb.service.UserService;

public class UsersControllerTest {

	private UserService users;
	
	private UsersController tested;
	
	@Before
	public void setup() {
		users = Mockito.mock(UserService.class);
		tested = new UsersController(users);
	}
	
	
}
