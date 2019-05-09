package com.chunkymonkey.itb.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.chunkymonkey.itb.filter.DiscoveryClientFilter;
import com.netflix.discovery.DiscoveryClient.DiscoveryClientOptionalArgs;
import com.sun.jersey.api.client.filter.ClientFilter;

@Configuration
public class DiscoveryConfigurer {

private final Environment env;
	
	@Autowired
	public DiscoveryConfigurer(Environment env) {
		this.env = env;
	}
	
	@Bean
	public DiscoveryClientOptionalArgs discoveryArgs() {
		DiscoveryClientOptionalArgs args = new DiscoveryClientOptionalArgs();
		args.setAdditionalFilters(authFilters());
		return args;
	}
	
	public List<ClientFilter> authFilters() {
		List<ClientFilter> filters = new ArrayList<>();
		filters.add(new DiscoveryClientFilter(
				env.getProperty("itb.config-username"), env.getProperty("itb.config-password")));
		return filters;
	}
	
}
