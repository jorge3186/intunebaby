package com.chunkymonkey.itb.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.chunkymonkey.itb.domain.BabyEntity;
import com.chunkymonkey.itb.dto.Baby;

@Service
public class MapperServiceImpl implements MapperService, InitializingBean {

	private final ModelMapper mapper = new ModelMapper();
	
	@Override
	public <T, S> T map(S source, Class<T> targetClass) {
		return mapper.map(source, targetClass);
	}

	@Override
	public <T, S> T map(S source, T target) {
		mapper.map(source,  target);
		return target;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// BabyEntity -> Baby
		mapper.addMappings(new PropertyMap<BabyEntity, Baby>() {
			@Override
			protected void configure() {
				map().setBirthday(source.getBirthday());
				map().setId(source.getId());
				map().setFirstName(source.getFirstName());
				map().setMiddleName(source.getMiddleName());
				map().setLastName(source.getLastName());
				map().setUsername(source.getAccountName());
				map().setGender(source.getGender());
			}
		});
	}

}
