package com.chunkymonkey.itb.service;

public interface MapperService {

	<T, S> T map(S source, Class<T> targetClass);
	
	<T, S> T map(S source, T target);
	
}
