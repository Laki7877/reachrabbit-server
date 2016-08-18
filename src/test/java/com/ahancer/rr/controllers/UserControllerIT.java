package com.ahancer.rr.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ahancer.rr.ReachrabbitServerApplication;

public class UserControllerIT extends AbstractControllerIT {
	@Test
	public void Should_Succeed_With_User_When_GetOneUser() {
		givenAdmin()
		.expect()
			.statusCode(200)
		.when()
			.get("/users/1");
	}
}
