package com.ahancer.rr.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ahancer.rr.ReachrabbitServerApplication;
import com.ahancer.rr.models.Bank;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.models.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ReachrabbitServerApplication.class)
@WebIntegrationTest
public class BrandControllerIT extends AbstractControllerIT {
	@Test
	public void createBrand() {
		User user = new User();
		Brand brand = new Brand();
		
		user.setName("I am tester");
		user.setEmail("test@test.com");
		user.setPassword("1234");
		user.setPhoneNumber("10000");
		
		brand.setBrandName("Tester Brand");
		brand.setUser(user);
	
		given()
			.body(brand)
		.expect()
			.statusCode(200)
			.body(containsString("token"))
		.when()
			.post("/signup/brand");	
	}
}
