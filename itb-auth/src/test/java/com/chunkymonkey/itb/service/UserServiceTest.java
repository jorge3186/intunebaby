package com.chunkymonkey.itb.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.Validation;
import javax.validation.ValidationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.chunkymonkey.itb.domain.ItbUser;
import com.chunkymonkey.itb.repository.UserRepository;
import com.chunkymonkey.itb.validation.ItbValidator;
import com.chunkymonkey.itb.validation.ItbValidatorImpl;

public class UserServiceTest {
	
	private UserRepository repo;
	
	private PasswordEncoder encoder;
	
	private UserService tested;
	
	private ItbValidator validator;
	
	private List<ItbUser> mockList = new ArrayList<>();
	
	@Before
	public void setup() {
		repo = Mockito.mock(UserRepository.class);
		encoder = new BCryptPasswordEncoder();
		validator = new ItbValidatorImpl(Validation.buildDefaultValidatorFactory().getValidator());
		
		tested = new UserServiceImpl(repo, encoder, validator);
		
		mockList.add(existingUser().get());
		var u2 = existingUser().get();
		u2.setUsername("another-user");
		mockList.add(u2);
		
		Mockito.when(repo.findById("existing-user"))
			.thenReturn(existingUser());
		Mockito.when(repo.save(Mockito.any()))
			.thenAnswer(new Answer<ItbUser>() {
				@Override
				public ItbUser answer(InvocationOnMock invocation) throws Throwable {
					return invocation.getArgument(0);
				}
			});
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				ItbUser u = invocation.getArgument(0);
				mockList.remove(u);
				return null;
			}
			
		}).doNothing().when(repo).delete(Mockito.any());
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				String uname = invocation.getArgument(0);
				var opt = mockList.stream()
					.filter(u -> uname.equals(u.getUsername()))
					.findAny();
				if (opt.isPresent())
					mockList.remove(opt.get());
				return null;
			}
			
		}).doNothing().when(repo).deleteById(Mockito.any());
		
	}
	
	@Test
	public void findUserByUserName() {
		var user = tested.findByUserName("existing-user");
		Assert.assertTrue("email@example.com".equals(user.getEmail()));
	}
	
	@Test
	public void findNonUserByName() {
		var user = tested.findByUserName("empty-user");
		Assert.assertTrue(user == null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testExistingUserCreate() {
		tested.create(existingUser().get());
	}
	
	@Test
	public void testCreateValidUser() {
		var user = existingUser().get();
		user.setUsername("new-user");
		var rawPw = user.getPassword();
		var created = tested.create(user);
		
		Assert.assertTrue("new-user".equals(created.getUsername()));
		Assert.assertTrue("email@example.com".equals(created.getEmail()));
		Assert.assertTrue(encoder.matches(rawPw, created.getPassword()));
	}
	
	@Test(expected = ValidationException.class)
	public void testCreateInvalidUser() {
		var user = existingInvalidUser().get();
		user.setUsername("new-user");
		tested.create(user);
	}
	
	@Test
	public void testUpdateExistingUser() {
		var user = existingUser().get();
		user.setPassword("newpassword");
		var updated = tested.update(user);
		
		Assert.assertTrue("existing-user".equals(updated.getUsername()));
		Assert.assertTrue(encoder.matches("newpassword", updated.getPassword()));
	}
	
	@Test(expected = ValidationException.class)
	public void testUpdateInvalidExistingUser() {
		var user = existingInvalidUser().get();
		user.setPassword("newpassword");
		tested.update(user);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateNonExistentUser() {
		var user = existingUser().get();
		user.setUsername("new-user");
		user.setPassword("newpassword");
		tested.update(user);
	}
	
	@Test
	public void deleteValidUser() {
		Assert.assertTrue(Integer.valueOf(2).equals(mockList.size()));
		tested.delete(existingUser().get());
		Assert.assertTrue(Integer.valueOf(1).equals(mockList.size()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void deleteNonExistentUser() {
		Assert.assertTrue(Integer.valueOf(2).equals(mockList.size()));
		var user = existingUser().get();
		user.setUsername("new-user");
		tested.delete(user);
	}
	
	@Test
	public void deleteUserByUsername() {
		Assert.assertTrue(Integer.valueOf(2).equals(mockList.size()));
		tested.delete("existing-user");
		Assert.assertTrue(Integer.valueOf(1).equals(mockList.size()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void deleteNonExistentUserByUsername() {
		Assert.assertTrue(Integer.valueOf(2).equals(mockList.size()));
		tested.delete("noone");
	}
	
	private Optional<ItbUser> existingUser() {
		var user = new ItbUser();
		user.setUsername("existing-user");
		user.setPassword("supersecret");
		user.setEmail("email@example.com");
		user.setAuthorities(Collections.emptyList());
		return Optional.of(user);
	}
	
	private Optional<ItbUser> existingInvalidUser() {
		var user = new ItbUser();
		user.setUsername("existing-user");
		user.setPassword("supersecret");
		user.setEmail("emailexamplecom");
		return Optional.of(user);
	}

}
