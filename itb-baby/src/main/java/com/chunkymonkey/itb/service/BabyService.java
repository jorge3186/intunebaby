package com.chunkymonkey.itb.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.chunkymonkey.itb.constants.SecurityConstants;
import com.chunkymonkey.itb.domain.BabyEntity;

public interface BabyService {

	@PreAuthorize(SecurityConstants.SERVER_SCOPE)
	BabyEntity create(BabyEntity baby);
	
	@PreAuthorize(SecurityConstants.SERVER_SCOPE)
	BabyEntity update(BabyEntity baby);
	
	@PreAuthorize(SecurityConstants.SERVER_SCOPE)
	void delete(String babyId);
	
	List<BabyEntity> getBabiesForUser(String username);
}
