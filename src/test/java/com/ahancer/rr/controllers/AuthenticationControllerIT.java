package com.ahancer.rr.controllers;

import static io.restassured.RestAssured.given;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ahancer.rr.ReachrabbitServerApplication;
import com.ahancer.rr.models.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ReachrabbitServerApplication.class)
@WebIntegrationTest
public class AuthenticationControllerIT extends AbstractControllerIT{
	@Test
	public void loginWithWrongUser() {
		User user = new User();
		user.setEmail("wrong@email.com");
		user.setPassword("wrong");
		
		//Call endpoint
		given()
			.body(user)
		.expect()
			.statusCode(400)
		.when()
			.post("/auth/login");
	}
}
