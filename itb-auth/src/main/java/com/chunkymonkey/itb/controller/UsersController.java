package com.chunkymonkey.itb.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chunkymonkey.itb.domain.ItbUser;
import com.chunkymonkey.itb.service.UserService;

@RestController
@RequestMapping("/v1")
public class UsersController {

	private final UserService users;
	
	@Autowired
	public UsersController(UserService users) {
		this.users = users;
	}
	
	@RequestMapping({ "/current", "/me" })
	public OAuth2Authentication currentUser(OAuth2Authentication user) {
		return user;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ItbUser> createUser(@Valid @RequestBody ItbUser user) {
		return ResponseEntity.ok(users.create(user));
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ItbUser> updateUser(@Valid @RequestBody ItbUser user) {
		return ResponseEntity.ok(users.update(user));
	}
	
	@RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteUser(@PathVariable("username") String username) {
		users.delete(username);
		return ResponseEntity.ok().build();
	}
}
