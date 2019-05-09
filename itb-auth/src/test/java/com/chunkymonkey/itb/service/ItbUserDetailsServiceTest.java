package com.chunkymonkey.itb.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.chunkymonkey.itb.domain.ItbUser;
import com.chunkymonkey.itb.repository.UserRepository;

public class ItbUserDetailsServiceTest {

	private UserRepository users;
	
	private UserDetailsService tested;
	
	@Before
	public void setup() {
		users = Mockito.mock(UserRepository.class);
		tested = new ItbUserDetailsService(users);
		
		Mockito.when(users.findById("noname"))
			.thenReturn(Optional.empty());
		Mockito.when(users.findById("user"))
			.thenReturn(mockRepoFind());
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void testUserNotFound() {
		tested.loadUserByUsername("noname");
	}
	
	@Test
	public void testUserLoaded() {
		var user = tested.loadUserByUsername("user");
		Assert.assertTrue(user instanceof ItbUser);
		Assert.assertTrue("username".equals(((ItbUser)user).getUsername()));
		Assert.assertTrue("email@example.com".equals(((ItbUser)user).getEmail()));
	}
	
	private Optional<ItbUser> mockRepoFind() {
		var u = new ItbUser();
		u.setUsername("username");
		u.setEmail("email@example.com");
		return Optional.of(u);
	}
}
