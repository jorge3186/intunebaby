package com.chunkymonkey.itb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chunkymonkey.itb.domain.ItbBabyError;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;

@RestController
public class GraphController {

	private final GraphQL graphQL;
	
	private final GraphQLSchema graphQLSchema;
	
	@Autowired
	public GraphController(GraphQL graphQL, GraphQLSchema schema) {
		this.graphQL = graphQL;
		this.graphQLSchema = schema;
	}
	
	@PostMapping("/gql")
	public ResponseEntity<Object> graphQL(@RequestBody String graphRequest) {
		if (graphRequest == null)
			return ResponseEntity.badRequest().build();
		
		ExecutionResult result = graphQL.execute(graphRequest);
		
		if (result.getErrors() != null && !result.getErrors().isEmpty())
			return handleError(result.getErrors());
		
		return ResponseEntity.ok(result.getData());
	}
	
	@GetMapping("/gql/schema")
	public ResponseEntity<String> graphQLSchema() {
		return ResponseEntity.ok(new SchemaPrinter().print(graphQLSchema));
	}
	
	private ResponseEntity<Object> handleError(List<GraphQLError> errors) {
		ItbBabyError err = new ItbBabyError();
		if (errors.get(0).getMessage().contains("Access is denied")) {
			err.setStatus(HttpStatus.FORBIDDEN);
			err.setMessage(errors.get(0).getErrorType().toString());
			err.setDetail(errors.get(0).getMessage());
			return new ResponseEntity<Object>(err, err.getStatus());
		} else {
			err.setStatus(HttpStatus.BAD_REQUEST);
			err.setMessage(errors.get(0).getErrorType().toString());
			err.setDetail(errors.get(0).getMessage());
			return new ResponseEntity<Object>(err, err.getStatus());
		}
	}
	
}
