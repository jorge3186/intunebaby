package com.chunkymonkey.itb.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chunkymonkey.itb.domain.BabyEntity;
import com.chunkymonkey.itb.graphql.domain.BabyInput;
import com.chunkymonkey.itb.service.BabyService;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;

@Component
public class BabyGraphQLService {

	private final BabyService babies;
	
	@Autowired
	public BabyGraphQLService(BabyService babies) {
		this.babies = babies;	
	}
	
	@GraphQLQuery(description = "Get all babies for given username")
	public List<BabyEntity> getBabiesForUser(@GraphQLArgument(name = "username") String username) {
		return babies.getBabiesForUser(username);
	}
	
	@GraphQLMutation(description = "Create a baby for the given user")
	public BabyEntity createBaby(@GraphQLArgument(name = "baby") BabyInput baby) {
		return babies.create(baby.toBaby());
	}
}
