package com.chunkymonkey.itb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.chunkymonkey.itb.service.ItbUserDetailsService;

@Configuration
public class OAuth2ServerConfigurer extends AuthorizationServerConfigurerAdapter {

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private ItbUserDetailsService userDetailsService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.authenticationManager(authenticationManager)
			.tokenStore(tokenStore)
			.accessTokenConverter(accessTokenConverter)
			.userDetailsService(userDetailsService);
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()")
			.passwordEncoder(encoder);
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			
			// User Interface
			.withClient("browser")
			.authorizedGrantTypes("refresh_token", "password")
			.scopes("ui")
			
			// itb-baby service
			.and()
			.withClient("itb-baby")
			.secret(encoder.encode(env.getProperty("itb.baby-password")))
			.authorizedGrantTypes("client_credentials", "refresh_token")
			.scopes("server")
			
			// itb-diapers service
			.and()
			.withClient("itb-diapers")
			.secret(encoder.encode(env.getProperty("itb.diapers-password")))
			.authorizedGrantTypes("client_credentials", "refresh_token")
			.scopes("server")
			
			// itb-feedings service
			.and()
			.withClient("itb-feedings")
			.secret(encoder.encode(env.getProperty("itb.feedings-password")))
			.authorizedGrantTypes("client_credentials", "refresh_token")
			.scopes("server")
			
			// itb-sleep service
			.and()
			.withClient("itb-sleep")
			.secret(encoder.encode(env.getProperty("itb.sleep-password")))
			.authorizedGrantTypes("client_credentials", "refresh_token")
			.scopes("server")
			
			// itb-growth service
			.and()
			.withClient("itb-sleep")
			.secret(encoder.encode(env.getProperty("itb.growth-password")))
			.authorizedGrantTypes("client_credentials", "refresh_token")
			.scopes("server")
			
			// itb-notify service
			.and()
			.withClient("itb-notify")
			.secret(encoder.encode(env.getProperty("itb.notify-password")))
			.authorizedGrantTypes("client_credentials", "refresh_token")
			.scopes("server");
	}
}
