package com.ahancer.rr.controllers;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ahancer.rr.ReachrabbitServerApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ReachrabbitServerApplication.class)
@WebIntegrationTest
public class CampaignControllerIT extends AbstractControllerIT {
	@Test
	public void Should_Fail_With_401_When_CreateCampaign_WithInfluencer() {
		Map<String, Object> campaign = new HashMap<>();
		givenInfluencer()
			.body(campaign)
		.expect()
			.statusCode(401)
		.when()
			.post("/campaigns");
		
	}
}
