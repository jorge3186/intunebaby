package com.chunkymonkey.itb.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
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
	public ResponseEntity<Void> createUser(@Valid @RequestBody ItbUser user) {
		users.create(user);
		return ResponseEntity.ok().build();
	}
}
