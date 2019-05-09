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
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;

public class GraphControllerTest {

	private GraphQL graphQL;
	
	private GraphQLSchema schema;
	
	private GraphController tested;
	
	@Before
	public void setup() {
		graphQL = Mockito.mock(GraphQL.class);
		schema = Mockito.mock(GraphQLSchema.class);
		tested = new GraphController(graphQL, schema);
		
		Mockito.when(graphQL.execute(Mockito.anyString()))
			.thenAnswer(new Answer<ExecutionResult>() {
				@Override
				public ExecutionResult answer(InvocationOnMock invocation) throws Throwable {
					String init = invocation.getArgument(0);
					if ("show errors".equals(init)) {
						return errors();
					} else if ("empty errors".equals(init)) {
						return emptyResponse();
					} else if ("auth errors".endsWith(init)) {
						return deniedErrors();
					} else {
						return validResponse();
					}
				}});
		
		Mockito.when(schema.getCodeRegistry())
			.thenReturn(GraphQLCodeRegistry.newCodeRegistry().build());
		Mockito.when(schema.getAllTypesAsList())
			.thenReturn(new ArrayList<GraphQLType>());
	}
	
	@Test
	public void testErrorsFormatted() throws JsonProcessingException {
		var obj = tested.graphQL("show errors");
		Assert.assertTrue(HttpStatus.BAD_REQUEST.equals(obj.getStatusCode()));
		Assert.assertTrue(new ObjectMapper().writeValueAsString(obj.getBody()).contains("\"message\":\"InvalidSyntax\""));
	}
	
	@Test
	public void testEmptyErrors() throws JsonProcessingException {
		var obj = tested.graphQL("empty errors");
		Assert.assertTrue(HttpStatus.OK.equals(obj.getStatusCode()));
	}
	
	@Test
	public void testNullRequest() {
		var obj = tested.graphQL(null);
		Assert.assertTrue(HttpStatus.BAD_REQUEST.equals(obj.getStatusCode()));
	}
	
	@Test
	public void testAuthRequest() {
		var obj = tested.graphQL("auth errors");
		Assert.assertTrue(HttpStatus.FORBIDDEN.equals(obj.getStatusCode()));
	}
	
	@Test
	public void testSchemaPrint() {
		var obj = tested.graphQLSchema();
		Assert.assertTrue(HttpStatus.OK.equals(obj.getStatusCode()));
		System.out.println(obj.getBody());
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
	
	private ExecutionResult deniedErrors() {
		return new ExecutionResult() {
			@Override
			public List<GraphQLError> getErrors() {
				List<GraphQLError> errs = new ArrayList<>();
				errs.add(new GraphQLError() {
					private static final long serialVersionUID = -3446602025203616944L;
					
					@Override
					public String getMessage() {
						return "Access is denied";
					}
					@Override
					public List<SourceLocation> getLocations() {
						return new ArrayList<>();
					}
					@Override
					public ErrorClassification getErrorType() {
						return ErrorType.DataFetchingException;
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
	
	private ExecutionResult emptyResponse() {
		return new ExecutionResult() {

			@Override
			public List<GraphQLError> getErrors() {
				return new ArrayList<>();
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
			}
			
		};
	}
}
