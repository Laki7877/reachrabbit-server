package com.ahancer.rr.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ahancer.rr.ReachrabbitServerApplication;
import com.ahancer.rr.utils.JwtUtil;

import io.restassured.response.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ReachrabbitServerApplication.class)
@WebIntegrationTest
public class AuthenticationControllerIT extends AbstractControllerIT{
	@Autowired
	private JwtUtil jwtUtil;
	@Test
	public void Should_Succeed_With_Token_When_BrandLogin() {
		Map<String,Object> user = new HashMap<>();
		user.put("email", "brand@reachrabbit.com");
		user.put("password", "1234");
		
		//Call endpoint
		Response response =
		given()
			.body(user)
		.expect()
			.statusCode(200)
			.body("token", allOf(not(isEmptyString()), instanceOf(String.class)))
		.when()
			.post("/auth/login")
		.then()
		.extract()
			.response();
		
		String token = response.jsonPath().getString("token");
		assertThat(jwtUtil.getUserId(token), instanceOf(Long.class));
	}
	@Test
	public void Should_Fail_With_400_When_BrandLogin_WithWrongUser() {
		Map<String,Object> user = new HashMap<>();
		user.put("email", "wrong@email.com");
		user.put("password", "1234321");
		
		//Call endpoint
		given()
			.body(user)
		.expect()
			.statusCode(400)
		.when()
			.post("/auth/login");
	}
	@Test
	public void Should_Fail_With_400_When_BrandLogin_WithWrongEmail() {
		Map<String,Object> user = new HashMap<>();
		user.put("email", "wrong@email.com");
		user.put("password", "1234");
		
		//Call endpoint
		given()
			.body(user)
		.expect()
			.statusCode(400)
		.when()
			.post("/auth/login");
	}
	@Test
	public void Should_Fail_With_400_When_BrandLogin_WithWrongPassword() {
		Map<String,Object> user = new HashMap<>();
		user.put("email", "brand@reachrabbit.com");
		user.put("password", "123456");
		
		//Call endpoint
		given()
			.body(user)
		.expect()
			.statusCode(400)
		.when()
			.post("/auth/login");
	}
}
