package com.chunkymonkey.itb.service;

import org.springframework.security.access.prepost.PreAuthorize;

import com.chunkymonkey.itb.constants.SecurityConstants;
import com.chunkymonkey.itb.domain.ItbUser;

/**
 * User Service for managing users of the application.
 * Only authenticated users with an oauth2 scope of 
 * 'server' can manipulate user data.<br><br>
 * 
 * This service can create, update, and delete user
 * information.
 *  
 * @author alphonso.jordan2
 *
 */
public interface UserService {

	/**
	 * Create a user for accessing the application.<br>
	 * User names must be unique in order to be added
	 * to the application.
	 * 
	 * @param user
	 * @return the newly created {@linkplain ItbUser}
	 */
	@PreAuthorize(SecurityConstants.SERVER_SCOPE)
	ItbUser create(ItbUser user);
	
	@PreAuthorize(SecurityConstants.SERVER_SCOPE)
	ItbUser update(ItbUser user);
	
	@PreAuthorize(SecurityConstants.SERVER_SCOPE)
	void delete(ItbUser user);
	
	@PreAuthorize(SecurityConstants.SERVER_SCOPE)
	void delete(String username);
	
}
