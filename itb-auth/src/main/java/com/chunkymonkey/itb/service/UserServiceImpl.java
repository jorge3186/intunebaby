package com.chunkymonkey.itb.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.chunkymonkey.itb.domain.ItbUser;
import com.chunkymonkey.itb.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private final UserRepository users;

	private final PasswordEncoder encoder;
	
	@Autowired
	public UserServiceImpl(UserRepository users, PasswordEncoder encoder) {
		this.users = users;
		this.encoder = encoder;
	}
	
	@Override
	public ItbUser findByUserName(String username) {
		Assert.notNull(username, "Username cannot be null");
		return users.findById(username)
			.orElse(null);
	}
	
	@Override
	public ItbUser create(ItbUser user) {
		users.findById(user.getUsername())
			.ifPresent(u-> {
				throw new IllegalArgumentException(
						String.format("User with username %s already exists", u.getUsername()));
			});
		
		logger.info(String.format("new user added with username: %s", user.getUsername()));
		user.setPassword(encoder.encode(user.getPassword()));
		return users.save(user);
	}

	@Override
	public ItbUser update(ItbUser user) {
		Optional<ItbUser> current = users.findById(user.getUsername());
		if (current.isPresent()) {
			ItbUser u = current.get();
			u.setAuthorities(user.getAuthorities());
			if (!u.getPassword().equals(user.getPassword()))
				u.setPassword(encoder.encode(user.getPassword()));
			return users.save(u);
		}
		return user;
	}

	@Override
	public void delete(ItbUser user) {
		Optional<ItbUser> current = users.findById(user.getUsername());
		if (current.isPresent())
			users.delete(user);
	}

	@Override
	public void delete(String username) {
		Optional<ItbUser> current = users.findById(username);
		if (current.isPresent())
			users.deleteById(username);
	}

}
