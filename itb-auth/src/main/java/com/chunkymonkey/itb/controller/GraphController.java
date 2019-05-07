package com.chunkymonkey.itb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import graphql.ExecutionResult;
import graphql.GraphQL;

@RestController
public class GraphController {

	private final GraphQL graphQL;
	
	@Autowired
	public GraphController(GraphQL graphQL) {
		this.graphQL = graphQL;
	}
	
	@PostMapping("/gql")
	public ResponseEntity<Object> graphQL(@RequestBody String graphRequest) {
		if (graphRequest == null)
			return ResponseEntity.badRequest().build();
		
		ExecutionResult result = graphQL.execute(graphRequest);
		
		if (result.getErrors() != null && !result.getErrors().isEmpty())
			return ResponseEntity
					.badRequest()
					.body(result.getErrors());
		
		return ResponseEntity.ok(result.getData());
	}
	
}
