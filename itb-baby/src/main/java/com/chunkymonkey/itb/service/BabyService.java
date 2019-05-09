package com.chunkymonkey.itb.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.chunkymonkey.itb.domain.BabyEntity;

public interface BabyService {

	@PreAuthorize("#oauth2.hasScope('server') or #baby.accountName == authentication.name")
	BabyEntity create(BabyEntity baby);
	
	@PreAuthorize("#oauth2.hasScope('server') or #baby.accountName == authentication.name")
	BabyEntity update(BabyEntity baby);
	
	@PreAuthorize("#oauth2.hasScope('server') or #baby.accountName == authentication.name")
	void delete(BabyEntity baby);
	
	@PreAuthorize("isAuthenticated() and #username == authentication.name")
	List<BabyEntity> getBabiesForUser(String username);
}
