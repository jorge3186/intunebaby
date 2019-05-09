package com.chunkymonkey.itb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.chunkymonkey.itb.domain.ItbUser;
import com.chunkymonkey.itb.repository.UserRepository;
import com.chunkymonkey.itb.validation.ItbValidator;

@Service
public class UserServiceImpl implements UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private final UserRepository users;

	private final PasswordEncoder encoder;

	private final ItbValidator validator;
	
	@Autowired
	public UserServiceImpl(UserRepository users, PasswordEncoder encoder, ItbValidator validator) {
		this.users = users;
		this.encoder = encoder;
		this.validator = validator;
	}
	
	@Override
	public ItbUser findByUserName(String username) {
		Assert.notNull(username, "Username cannot be null");
		return users.findById(username)
			.orElse(null);
	}
	
	@Override
	public ItbUser create(ItbUser user) {
		Assert.notNull(user, "User cannot be null");
		users.findById(user.getUsername())
			.ifPresent(u-> {
				throw new IllegalArgumentException(
						String.format("User with username %s already exists", u.getUsername()));
			});
		
		validator.validate(user);
		logger.info(String.format("new user added with username: %s", user.getUsername()));
		user.setPassword(encoder.encode(user.getPassword()));
		return users.save(user);
	}

	@Override
	public ItbUser update(ItbUser user) {
		Assert.notNull(user, "User cannot be null");
		var entity = users.findById(user.getUsername())
			.orElseThrow(() -> new IllegalArgumentException(String.format("No user found with id: %s", user.getUsername())));
		
		validator.validate(user);
		entity.setAuthorities(user.getAuthorities());
		if (!entity.getPassword().equals(user.getPassword()) && !encoder.matches(user.getPassword(), entity.getPassword()))
			entity.setPassword(encoder.encode(user.getPassword()));
			users.save(entity);
		return entity;
	}

	@Override
	public void delete(ItbUser user) {
		var entity = users.findById(user.getUsername())
			.orElseThrow(() -> new IllegalArgumentException(String.format("No user found with id: %s", user.getUsername())));
		users.delete(entity);
	}

	@Override
	public void delete(String username) {
		var entity = users.findById(username)
			.orElseThrow(() -> new IllegalArgumentException(String.format("No user found with id: %s", username)));
		users.delete(entity);
	}

}
