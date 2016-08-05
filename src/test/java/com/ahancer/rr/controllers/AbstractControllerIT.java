package com.ahancer.rr.controllers;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.BrandDao;
import com.ahancer.rr.daos.InfluencerDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.User;
import com.ahancer.rr.utils.EncryptionUtil;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;

public abstract class AbstractControllerIT {

    @Value("${server.port}") 
    private int port;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BrandDao brandDao;
	
	@Autowired
	private InfluencerDao influencerDao;
	
	@Autowired
	private EncryptionUtil encryptionUtil;
	
	protected String adminToken;
	protected String brandToken;
	protected String influencerToken;
	
	public void setupRestAssured() {
		LogConfig logConfig = LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL).enablePrettyPrinting(true);
		
		RestAssured.port = port;
		RestAssured.config = RestAssured.config()
			.logConfig(logConfig);
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
	}
	
	@Before
	public void before() {
		//Setup test framework
		setupRestAssured();
		
		//Create admin
		User admin = new User();
		admin.setName("PLK THE ADMIN");
		admin.setEmail("admin@reachrabbit.com");
		admin.setPassword(encryptionUtil.hashPassword("1234"));
		admin.setRole(Role.Admin);
		
		userDao.save(Arrays.asList(admin));
	}
	
	@After
	public void after() {
		influencerDao.deleteAll();
		brandDao.deleteAll();
		userDao.deleteAll();
	}
}
