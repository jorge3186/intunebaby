package com.chunkymonkey.itb.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class MapperServiceImpl implements MapperService, InitializingBean {

	private final ModelMapper mapper = new ModelMapper();

	@Override
	public <T, S> T map(S source, Class<T> targetClass) {
		return mapper.map(source, targetClass);
	}

	@Override
	public <T, S> T map(S source, T target) {
		mapper.map(source, target);
		return target;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
}
