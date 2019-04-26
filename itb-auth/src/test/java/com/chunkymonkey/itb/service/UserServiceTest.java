package com.chunkymonkey.itb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

public class UserServiceTest {
	
	private UserRepository repo;
	
	private PasswordEncoder encoder;
	
	private UserService tested;
	
	private List<ItbUser> mockList = new ArrayList<>();
	
	@Before
	public void setup() {
		repo = Mockito.mock(UserRepository.class);
		encoder = new BCryptPasswordEncoder();
		tested = new UserServiceImpl(repo, encoder);
		
		mockList.add(existingUser().get());
		ItbUser u2 = existingUser().get();
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
				ItbUser u = (ItbUser)invocation.getArgument(0);
				mockList.remove(u);
				return null;
			}
			
		}).doNothing().when(repo).delete(Mockito.any());
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				String uname = (String)invocation.getArgument(0);
				Optional<ItbUser> opt = mockList.stream()
					.filter(u -> uname.equals(u.getUsername()))
					.findAny();
				if (opt.isPresent())
					mockList.remove(opt.get());
				return null;
			}
			
		}).doNothing().when(repo).deleteById(Mockito.any());
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testExistingUserCreate() {
		tested.create(existingUser().get());
	}
	
	@Test
	public void testCreateValidUser() {
		ItbUser user = existingUser().get();
		user.setUsername("new-user");
		String rawPw = user.getPassword();
		ItbUser created = tested.create(user);
		
		Assert.assertTrue("new-user".equals(created.getUsername()));
		Assert.assertTrue("email@example.com".equals(created.getEmail()));
		Assert.assertTrue(encoder.matches(rawPw, created.getPassword()));
	}
	
	@Test
	public void testUpdateExistingUser() {
		ItbUser user = existingUser().get();
		user.setPassword("newpassword");
		ItbUser updated = tested.update(user);
		
		Assert.assertTrue("existing-user".equals(updated.getUsername()));
		Assert.assertTrue(encoder.matches("newpassword", updated.getPassword()));
	}
	
	@Test
	public void testUpdateNonExistentUser() {
		ItbUser user = existingUser().get();
		user.setUsername("new-user");
		user.setPassword("newpassword");
		ItbUser updated = tested.update(user);
		
		Assert.assertTrue("new-user".equals(updated.getUsername()));
		// encoding process is skipped if user does not exists
		Assert.assertTrue("newpassword".equals(updated.getPassword()));
	}
	
	@Test
	public void deleteValidUser() {
		Assert.assertTrue(new Integer(2).equals(mockList.size()));
		tested.delete(existingUser().get());
		Assert.assertTrue(new Integer(1).equals(mockList.size()));
	}
	
	@Test
	public void deleteNonExistentUser() {
		Assert.assertTrue(new Integer(2).equals(mockList.size()));
		ItbUser user = existingUser().get();
		user.setUsername("new-user");
		tested.delete(user);
		Assert.assertTrue(new Integer(2).equals(mockList.size()));
	}
	
	@Test
	public void deleteUserByUsername() {
		Assert.assertTrue(new Integer(2).equals(mockList.size()));
		tested.delete("existing-user");
		Assert.assertTrue(new Integer(1).equals(mockList.size()));
	}
	
	@Test
	public void deleteNonExistentUserByUsername() {
		Assert.assertTrue(new Integer(2).equals(mockList.size()));
		tested.delete("noone");
		Assert.assertTrue(new Integer(2).equals(mockList.size()));
	}
	
	private Optional<ItbUser> existingUser() {
		ItbUser user = new ItbUser();
		user.setUsername("existing-user");
		user.setPassword("supersecret");
		user.setEmail("email@example.com");
		return Optional.of(user);
	}

}
