package com.ahancer.rr.controllers;

import org.junit.Test;

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
