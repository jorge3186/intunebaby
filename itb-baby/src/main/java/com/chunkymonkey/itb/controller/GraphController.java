package com.chunkymonkey.itb.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;

@RestController
public class GraphController {

	private final GraphQL graphQL;
	
	@Autowired
	public GraphController(GraphQL graphQL) {
		this.graphQL = graphQL;
	}
	
	@PostMapping("/gql")
	public ResponseEntity<Object> graphQL(@RequestBody String graphRequest) {
		ExecutionResult result = graphQL.execute(graphRequest);
		
		if (!result.getErrors().isEmpty())
			return handleErrors(result.getErrors());
		
		return ResponseEntity.ok(result.getData());
	}
	
	private ResponseEntity<Object> handleErrors(List<GraphQLError> errors) {
		return ResponseEntity
				.badRequest()
				.body(errors.stream()
						.collect(Collectors.toMap(
								GraphQLError::getMessage, 
								GraphQLError::toSpecification)));
	}
	
}
