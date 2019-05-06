package com.chunkymonkey.itb.graphql;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.chunkymonkey.itb.config.GraphQLConfigurer;
import com.chunkymonkey.itb.domain.ItbUser;
import com.chunkymonkey.itb.service.UserService;

import graphql.ExecutionResult;
import graphql.GraphQL;

public class GraphQLServiceTest {

	private UserService users;
	
	private UsersGraphQLService service;
	
	private GraphQL tested;
	
	private List<ItbUser> mockList;
	
	@Before
	public void setup() {
		users = Mockito.mock(UserService.class);
		service = new UsersGraphQLService(users);
		
		tested = new GraphQLConfigurer(service).graphQL();
		
		mockList = new ArrayList<>();
		mockList.add(newUser());
		mockList.add(userNoAuthorities());
		
		Mockito.when(users.findByUserName("my.user"))
			.thenReturn(newUser());
		Mockito.when(users.findByUserName("ext.user"))
			.thenReturn(userNoAuthorities());
		Mockito.when(users.create(Mockito.any()))
			.thenAnswer(new Answer<ItbUser>() {
				@Override
				public ItbUser answer(InvocationOnMock invocation) throws Throwable {
					return invocation.getArgument(0);
				}});
		Mockito.when(users.update(Mockito.any()))
			.thenAnswer(new Answer<ItbUser>() {
				@Override
				public ItbUser answer(InvocationOnMock invocation) throws Throwable {
					ItbUser u = invocation.getArgument(0);
					ItbUser c = newUser();
					u.setUsername(c.getUsername());
					return u;
				}
			});
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				ItbUser obj = invocation.getArgument(0);
				mockList.remove(obj);
				return null;
			}
		}).doNothing().when(users).delete(Mockito.any(ItbUser.class));
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				String obj = invocation.getArgument(0);
				ItbUser user = mockList.stream()
					.filter(u -> u.getUsername().equals(obj))
					.findAny()
					.orElse(null);
				mockList.remove(user);
				return null;
			}
		}).doNothing().when(users).delete(Mockito.any(String.class));
	}
	
	@Test
	public void testUserByUsername() {
		ExecutionResult result = tested.execute("query { findByUsername(username: \"my.user\") { username email authorities } }");
		Assert.assertTrue(result.getData().toString().contains("email=my.user@example.com"));
		
		result = tested.execute("query { findByUsername(username: \"ext.user\") { username email authorities } }");
		Assert.assertTrue(result.getData().toString().contains("email=ext.user@example.com"));
		Assert.assertTrue(result.getData().toString().contains("authorities=[]"));
		
		result = tested.execute("query { findByUsername(username: \"no.user\") { username email authorities } }");
		Assert.assertTrue(result.getData().toString().contains("null"));
	}
	
	@Test
	public void testCreateUser() {
		ExecutionResult result = tested.execute("mutation { create(user: "
				+ "{ username: \"my.user\", password: \"pw\", email: \"my.emal@exmaple.com\", authorities: [\"ROLE_USER\", \"ROLE_YOURMOM\"] }) "
				+ "{ username email authorities } }");
		Assert.assertTrue(result.getData().toString().contains("ROLE_YOURMOM"));
	}
	
	@Test
	public void testUpdateUser() {
		ExecutionResult result = tested.execute("mutation { update(user: "
				+ "{ username: \"my.user\", password: \"updatedPW\", email: \"updated@exmaple.com\", authorities: [\"ROLE_USER\", \"ROLE_HISMOM\"] }) "
				+ "{ username email authorities } }");
		Assert.assertTrue(result.getData().toString().contains("username=my.user"));
		Assert.assertTrue(result.getData().toString().contains("email=updated@exmaple.com"));
		Assert.assertTrue(result.getData().toString().contains("ROLE_HISMOM"));
	}
	
	@Test
	public void testDeleteUser() {
		Assert.assertTrue(Integer.valueOf(2).equals(mockList.size()));
		ExecutionResult result = tested.execute("mutation { delete(user: "
				+ "{ username: \"my.user\", password: \"pw\", email: \"my.emal@exmaple.com\", authorities: [\"ROLE_USER\", \"ROLE_YOURMOM\"] }) }");
		System.out.println(result.getData().toString());
		Assert.assertTrue(result.getData().toString().contains("delete=my.user"));
		Assert.assertTrue(Integer.valueOf(1).equals(mockList.size()));
	}
	
	@Test
	public void testDeleteUserByUsername() {
		Assert.assertTrue(Integer.valueOf(2).equals(mockList.size()));
		ExecutionResult result = tested.execute("mutation { deleteByUsername(username: \"my.user\") }");
		System.out.println(result.getData().toString());
		Assert.assertTrue(result.getData().toString().contains("deleteByUsername=my.user"));
		Assert.assertTrue(Integer.valueOf(1).equals(mockList.size()));
	}
	
	private ItbUser newUser() {
		List<GrantedAuthority> grants = new ArrayList<>();
		grants.add(new SimpleGrantedAuthority("ROLE_USER"));
		grants.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		ItbUser u = new ItbUser();
		u.setUsername("my.user");
		u.setEmail("my.user@example.com");
		u.setAuthorities(grants);
		return u;
	}
	
	private ItbUser userNoAuthorities() {
		ItbUser u = new ItbUser();
		u.setUsername("ext.user");
		u.setEmail("ext.user@example.com");
		return u;
	}
}
