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
	
	@Before
	public void setup() {
		users = Mockito.mock(UserService.class);
		service = new UsersGraphQLService(users);
		
		tested = new GraphQLConfigurer(service).graphQL();
		
		Mockito.when(users.findByUserName("my.user"))
			.thenReturn(newUser());
		Mockito.when(users.create(Mockito.any()))
			.thenAnswer(new Answer<ItbUser>() {
				@Override
				public ItbUser answer(InvocationOnMock invocation) throws Throwable {
					return invocation.getArgument(0);
				}});
	}
	
	@Test
	public void testUserByUsername() {
		ExecutionResult result = tested.execute("query { findByUsername(username: \"my.user\") { username email authorities } }");
		Assert.assertTrue(result.getData().toString().contains("my.user@example.com"));
	}
	
	@Test
	public void testCreateUser() {
		ExecutionResult result = tested.execute("mutation { create(user: "
				+ "{ username: \"my.user\", password: \"pw\", email: \"my.emal@exmaple.com\", authorities: [\"ROLE_USER\", \"ROLE_YOURMOM\"] }) "
				+ "{ username email authorities } }");
		Assert.assertTrue(result.getData().toString().contains("ROLE_YOURMOM"));
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
}
