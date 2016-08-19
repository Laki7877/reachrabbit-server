package com.ahancer.rr.controllers;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.response.Response;

public class CampaignControllerIT extends AbstractControllerIT {

	@Test
	public void Should_Succeed_With_Campaign_When_CreateCampaign_WithBrand() {
		Map<String, Object> campaign = new HashMap<>();
		campaign.put("title", "test");
		campaign.put("description","desc");

		Response response =
				givenBrand()
				.body(campaign)
				.expect()
				.statusCode(200)
				.when()
				.post("/campaign")
				.then()
				.extract()
				.response();

		Object newCampaign = response.jsonPath().get();

		assertThat(newCampaign, allOf(
				hasProperty("title", equalTo(campaign.get("title"))),
				hasProperty("description", equalTo(campaign.get("description")))
				));
	}
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
