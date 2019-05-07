package com.chunkymonkey.itb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class GraphControllerTest {

	private GraphQL graphQL;
	
	private GraphController tested;
	
	@Before
	public void setup() {
		graphQL = Mockito.mock(GraphQL.class);
		tested = new GraphController(graphQL);
		
		Mockito.when(graphQL.execute(Mockito.anyString()))
			.thenAnswer(new Answer<ExecutionResult>() {
				@Override
				public ExecutionResult answer(InvocationOnMock invocation) throws Throwable {
					String init = invocation.getArgument(0);
					if ("show errors".equals(init)) {
						return errors();
					} else {
						return validResponse();
					}
				}});
	}
	
	@Test
	public void testErrorsFormatted() throws JsonProcessingException {
		ResponseEntity<Object> obj = tested.graphQL("show errors");
		Assert.assertTrue(HttpStatus.BAD_REQUEST.equals(obj.getStatusCode()));
		Assert.assertTrue(new ObjectMapper().writeValueAsString(obj.getBody()).contains("\"errorType\":\"InvalidSyntax\""));
	}
	
	@Test
	public void testNullRequest() {
		ResponseEntity<Object> obj = tested.graphQL(null);
		Assert.assertTrue(HttpStatus.BAD_REQUEST.equals(obj.getStatusCode()));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testValidResponse() {
		ResponseEntity<Object> obj = tested.graphQL("valid");
		Assert.assertTrue(HttpStatus.OK.equals(obj.getStatusCode()));
		Assert.assertTrue(Integer.valueOf(3).equals(((Map<String, String>)obj.getBody()).size()));
	}
	
	private ExecutionResult errors() {
		return new ExecutionResult() {
			@Override
			public List<GraphQLError> getErrors() {
				List<GraphQLError> errs = new ArrayList<>();
				errs.add(new GraphQLError() {
					private static final long serialVersionUID = -3446602025203616944L;
					
					@Override
					public String getMessage() {
						return "message 1";
					}
					@Override
					public List<SourceLocation> getLocations() {
						return new ArrayList<>();
					}
					@Override
					public ErrorClassification getErrorType() {
						return ErrorType.InvalidSyntax;
					}});
				return errs;
			}

			@Override
			public <T> T getData() {
				return null;
			}

			@Override
			public Map<Object, Object> getExtensions() {
				return null;
			}

			@Override
			public Map<String, Object> toSpecification() {
				Map<String, Object> m = new HashMap<>();
				m.put("spec1", "val1");
				m.put("spec2", "val2");
				return m;
			}};
	}
	
	private ExecutionResult validResponse() {
		return new ExecutionResult() {

			@Override
			public List<GraphQLError> getErrors() {
				return null;
			}

			@SuppressWarnings("unchecked")
			@Override
			public <T> T getData() {
				Map<String, String> m = new HashMap<>();
				m.put("data1", "value1");
				m.put("data2", "value2");
				m.put("data3", "value3");
				return (T)m;
			}

			@Override
			public Map<Object, Object> getExtensions() {
				return null;
			}

			@Override
			public Map<String, Object> toSpecification() {
				Map<String, Object> m = new HashMap<>();
				m.put("spec1", "val1");
				m.put("spec2", "val2");
				return m;
			}
			
		};
	}
}
