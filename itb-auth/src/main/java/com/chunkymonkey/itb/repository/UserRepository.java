package com.chunkymonkey.itb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chunkymonkey.itb.domain.ItbUser;

@Repository
public interface UserRepository extends MongoRepository<ItbUser, String> {
}
