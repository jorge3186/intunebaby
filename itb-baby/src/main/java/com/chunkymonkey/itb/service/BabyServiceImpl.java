package com.chunkymonkey.itb.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.chunkymonkey.itb.domain.BabyEntity;
import com.chunkymonkey.itb.repository.BabyRepository;
import com.chunkymonkey.itb.validation.ItbValidator;

@Service
public class BabyServiceImpl implements BabyService {

	private final static Logger logger = LoggerFactory.getLogger(BabyServiceImpl.class);
	
	private final BabyRepository babies;
	
	private final ItbValidator validator;
	
	@Autowired
	public BabyServiceImpl(BabyRepository babies, ItbValidator validator) {
		this.babies = babies;
		this.validator = validator;
	}
	
	@Override
	public BabyEntity create(BabyEntity baby) {
		babies.findById(baby.getId())
		.ifPresent(b-> {
			throw new IllegalArgumentException(
					String.format("Baby with id %s already exists", b.getId()));
		});
		
		validator.validate(baby);
		logger.info(String.format("new baby added with id: %s", baby.getId()));
		return babies.save(baby);
	}

	@Override
	public BabyEntity update(BabyEntity baby) {
		var entity = babies.findById(baby.getId())
			.orElseThrow(() -> new IllegalArgumentException(String.format("No baby found with id: %s", baby.getId())));
		
		validator.validate(baby);
		entity.setBirthday(baby.getBirthday());
		entity.setFirstName(baby.getFirstName());
		entity.setMiddleName(baby.getMiddleName());
		entity.setLastName(baby.getLastName());
		entity.setGender(baby.getGender());
		return babies.save(entity);
	}

	@Override
	public void delete(BabyEntity baby) {
		var entity = babies.findById(baby.getId())
				.orElseThrow(() -> new IllegalArgumentException(String.format("No baby found with id: %s", baby.getId())));
		babies.delete(entity);
	}

	@Override
	public List<BabyEntity> getBabiesForUser(String username) {
		Assert.notNull(username, "Username cannot be null");
		return babies.getBabiesForUser(username);
	}

}
