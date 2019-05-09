package com.chunkymonkey.itb.service;

import java.math.BigInteger;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.chunkymonkey.itb.domain.Sequences;

@Service
public class SequenceServiceImpl implements SequenceService {

	private final MongoOperations mongo;
	
	@Autowired
	public SequenceServiceImpl(MongoOperations mongo) {
		this.mongo = mongo;
	}
	
	@Override
	public BigInteger nextSequenceValue(String sequenceName) {
		Assert.notNull(sequenceName, "Sequence Name cannot be null");
		
		var counter = mongo.findAndModify(Query.query(Criteria.where("_id").is(sequenceName)), 
				new Update().inc("nextval", BigInteger.valueOf(1)), Sequences.class);
		return !Objects.isNull(counter) ? counter.getNextval() : BigInteger.valueOf(1);
	}

}
