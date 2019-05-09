package com.chunkymonkey.itb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chunkymonkey.itb.graphql.UsersGraphQLService;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;

@Configuration
public class GraphQLConfigurer {

	private final UsersGraphQLService graphQLService;
	
	@Autowired
	public GraphQLConfigurer(UsersGraphQLService graphQLService) {
		this.graphQLService = graphQLService;
	}
	
	@Bean
	public GraphQL graphQL() {
		return GraphQL.newGraphQL(graphQLSchema()).build();
	}
	
	@Bean 
	public GraphQLSchema graphQLSchema() {
		return new GraphQLSchemaGenerator()
				.withOperationsFromSingleton(graphQLService)
				.generate();
	}
}
