package com.chunkymonkey.itb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = {ItbBabyServer.class}, 
		properties = {
				"spring.cloud.vault.enabled=false", 
				"spring.cloud.config.enabled=false",
				"eureka.client.enabled=false"})
@ActiveProfiles({"test"})
public class ItbBabyServerTest {

	@LocalServerPort
	private int port;
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	@Test
	public void contextLoaded() throws Exception {}
	
}
