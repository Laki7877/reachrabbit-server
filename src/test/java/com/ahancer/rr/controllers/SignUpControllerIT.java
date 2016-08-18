package com.ahancer.rr.controllers;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ahancer.rr.ReachrabbitServerApplication;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.User;
import com.ahancer.rr.utils.EncryptionUtil;
import com.ahancer.rr.utils.JwtUtil;

import io.restassured.response.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ReachrabbitServerApplication.class)
@WebIntegrationTest
public class SignUpControllerIT extends AbstractControllerIT{
	@Autowired
	private UserDao userDao;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private EncryptionUtil eUtil;

	@Test
	public void Should_Succeed_With_Token_When_CreateNewBrand() {
		//Create brand
		Map<String, Object> user = new HashMap<>();
		Map<String, Object> brand = new HashMap<>();
		Map<String, Object> bank = new HashMap<>();
		
		bank.put("bankId", "002");
		brand.put("brandName", "Brand naming");
		
		user.put("name", "I am a tester");
		user.put("email", "test@test.com");
		user.put("password", "1234");
		user.put("phoneNumber", "1234");
		user.put("brand", brand);
		user.put("bankAccount", "testAccount");
		user.put("bank", bank);
		
		//Should return token for after login
		Response response =
		given()
			.body(user)
		.expect()
			.statusCode(200)
			.body("token", allOf(not(isEmptyString()), instanceOf(String.class)))
		.when()
			.post("/signup/brand")
		.then()
		.extract()
			.response();
		
		
		String token = response.jsonPath().getString("token");
		assertThat(jwtUtil.getUserId(token), instanceOf(Long.class));
		
		
		//Afterward, try get check if userExist
		User newUser = userDao.findByEmail("test@test.com");

		eUtil.checkPassword((String)user.get("password"), newUser.getPassword());
		assertThat(newUser, allOf(
				hasProperty("name", equalTo(user.get("name"))),
				hasProperty("email", equalTo(user.get("email"))),
				hasProperty("phoneNumber", equalTo(user.get("phoneNumber"))),
				hasProperty("brand", hasProperty("brandName", equalTo(brand.get("brandName")))),
				hasProperty("bankAccount", equalTo(user.get("bankAccount"))),
				hasProperty("bank", hasProperty("bankId", equalTo(bank.get("bankId"))))
		));
	}
	@Test
	public void Should_Fail_With_400_When_CreateNewBrand_WithDuplicateEmail() {
		Map<String, Object> user = new HashMap<>();
		user.put("email", "test@test");
		user.put("password", "1234");
		
		Map<String, Object> user2 = new HashMap<>();
		user2.put("email", "test@test");
		user2.put("password", "1234");

		//First user
		given()
			.body(user)
		.when()
			.post("/signup/brand");
		
		//Second user
		given()
			.body(user2)
		.expect()
			.statusCode(400)
		.when()
			.post("/signup/brand");
	}
	@Test
	public void Should_Fail_With_400_When_CreateNewBrand_WithoutEmail() {
		Map<String, Object> user = new HashMap<>();
		user.put("password", "1234");
		
		//First user
		given()
			.body(user)
		.expect()
			.statusCode(400)
		.when()
			.post("/signup/brand");
	}
	@Test
	public void Should_Fail_With_400_When_CreateNewBrand_WithoutPassword() {
		Map<String, Object> user = new HashMap<>();
		user.put("email", "test4@test");
		
		//First user
		given()
			.body(user)
		.expect()
			.statusCode(400)
		.when()
			.post("/signup/brand");
	}
	@Test
	public void Should_Fail_With_400_When_CreateNewBrand_WithEmptyObject() {
		Map<String, Object> user = new HashMap<>();
		
		//First user
		given()
			.body(user)
		.expect()
			.statusCode(400)
		.when()
			.post("/signup/brand");
	}
	
}
