package com.chunkymonkey.itb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.chunkymonkey.itb.domain.BabyEntity;

@Repository
public interface BabyRepository extends MongoRepository<BabyEntity, String> {

	@Query("{ '_id' : ?0 }")
	List<BabyEntity> getBabiesForUser(String username);
	
}
