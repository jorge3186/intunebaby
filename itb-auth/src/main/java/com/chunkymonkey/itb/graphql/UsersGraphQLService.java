package com.chunkymonkey.itb.graphql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import com.chunkymonkey.itb.domain.ItbUser;
import com.chunkymonkey.itb.graphql.domain.ItbAuthentication;
import com.chunkymonkey.itb.graphql.domain.ItbUserInput;
import com.chunkymonkey.itb.service.UserService;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;

@Component
public class UsersGraphQLService {

	private final UserService users;
	
	@Autowired
	public UsersGraphQLService(UserService users) {
		this.users = users;
	}
	
	@GraphQLQuery(description = "Get the current authentication object")
	public ItbAuthentication me() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && OAuth2Authentication.class.isAssignableFrom(auth.getClass()))
			return new ItbAuthentication((OAuth2Authentication)auth);
		return null;
	}
	
	@GraphQLQuery(description = "Get the principal of the current user")
	public Map<String, Object> principal(@GraphQLContext ItbAuthentication authentication) {
		var map = new HashMap<String, Object>();
		if (authentication != null) {
			var princ = authentication.getPrincipal();
			if (princ != null && princ instanceof User) {
				var user = (User)princ;
				map.put("username", user.getUsername());
				map.put("authorities", user.getAuthorities() == null ? new ArrayList<>() : 
					user.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList()));
			}
		}
		return map;
	}
	
	@GraphQLQuery(description = "Fetch a user by username")
	public ItbUser findByUsername(@GraphQLArgument(name="username") String username) {
		return users.findByUserName(username);
	}
	
	@GraphQLQuery
	public List<String> authorities(@GraphQLContext ItbUser user) {
		if (user == null || user.getAuthorities() == null)
			return new ArrayList<>();
		
		return user.getAuthorities().stream()
				.map(auth -> auth.getAuthority())
				.collect(Collectors.toList());
	}
	
	@GraphQLMutation(description = "Create a User for the ITB application")
	public ItbUser create(@GraphQLArgument(name = "user") ItbUserInput user) {
		return users.create(new ItbUser(
				user.getUsername(),
				user.getPassword(),
				user.getEmail(),
				user.getAuthorities()));
	}
	
	@GraphQLMutation(description = "Update a User for the ITB application")
	public ItbUser update(@GraphQLArgument(name = "user") ItbUserInput user) {
		return users.update(new ItbUser(
				user.getUsername(),
				user.getPassword(),
				user.getEmail(),
				user.getAuthorities()));
	}
	
	@GraphQLMutation(description = "Delete a User from the ITB application")
	public String delete(@GraphQLArgument(name = "user") ItbUserInput user) {
		users.delete(new ItbUser(
				user.getUsername(),
				user.getPassword(),
				user.getEmail(),
				user.getAuthorities()));
		return user.getUsername();
	}
	
	@GraphQLMutation(description = "Delete a User from the ITB application by username")
	public String deleteByUsername(@GraphQLArgument(name = "username") String username) {
		users.delete(username);
		return username;
	}
	
}
