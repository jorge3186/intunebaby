package com.chunkymonkey.itb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chunkymonkey.itb.repository.UserRepository;

@Service
public class ItbUserDetailsService implements UserDetailsService {

	private final UserRepository users;

	@Autowired
	public ItbUserDetailsService(UserRepository users) {
		this.users = users;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return users.findById(username)
				.orElseThrow(() -> new UsernameNotFoundException(
						String.format("User not found with username of %s", username)));
	}

}
