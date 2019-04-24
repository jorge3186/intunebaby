package com.chunkymonkey.itb.service;

import org.springframework.security.access.prepost.PreAuthorize;

import com.chunkymonkey.itb.constants.SecurityConstants;
import com.chunkymonkey.itb.domain.ItbUser;

public interface UserService {

	@PreAuthorize(SecurityConstants.SERVER_SCOPE)
	ItbUser create(ItbUser user);
	
	@PreAuthorize(SecurityConstants.SERVER_SCOPE)
	ItbUser update(ItbUser user);
	
	@PreAuthorize(SecurityConstants.SERVER_SCOPE)
	void delete(ItbUser user);
	
	@PreAuthorize(SecurityConstants.SERVER_SCOPE)
	void delete(String username);
	
}
