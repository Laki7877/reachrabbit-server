package com.ahancer.rr.controllers;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.ReachrabbitServerApplication;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.BrandDao;
import com.ahancer.rr.daos.InfluencerDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.models.Influencer;
import com.ahancer.rr.models.User;
import com.ahancer.rr.services.AuthenticationService;
import com.ahancer.rr.utils.EncryptionUtil;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringApplicationConfiguration(classes = ReachrabbitServerApplication.class)
@WebIntegrationTest
public abstract class AbstractControllerIT {
	
	@Autowired
	private HttpServletRequest request;

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

	protected User brand;
	protected User admin;
	protected User influencer;

	@Autowired
	protected AuthenticationService authenticationService;

	public void setupRestAssured() {
		LogConfig logConfig = LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL).enablePrettyPrinting(true);
		RestAssured.port = port;
		RestAssured.config = RestAssured.config()
				.logConfig(logConfig);
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
	}

	public RequestSpecification givenAdmin() {
		return RestAssured.given()
				.header("X-Auth-Token", adminToken);
	}
	public RequestSpecification givenBrand() {
		return RestAssured.given()
				.header("X-Auth-Token", brandToken);
	}
	public RequestSpecification givenInfluencer() {
		return RestAssured.given()
				.header("X-Auth-Token", influencerToken);
	}

	@Before()
	@Transactional
	public void before() {
		//Setup test framework
		setupRestAssured();
		try{
			//Create admin
			User admin = new User();
			admin.setName("PLK THE ADMIN");
			admin.setEmail("admin@reachrabbit.com");
			admin.setPassword(encryptionUtil.hashPassword("1234"));
			admin.setRole(Role.Admin);
			admin = userDao.save(admin);

			User brand = new User();
			Brand br = new Brand();
			brand.setName("Nattamoto brand");
			brand.setEmail("brand@reachrabbit.com");
			brand.setPassword(encryptionUtil.hashPassword("1234"));
			brand.setRole(Role.Brand);
			brand.setBrand(null);
			brand = userDao.save(brand);
			br.setUser(null);
			br.setBrandId(brand.getUserId());
			brand.setBrand(brandDao.save(br));

			User influencer = new User();
			Influencer inf = new Influencer();
			influencer.setName("Influencer");
			influencer.setEmail("influencer@reachrabbit.com");
			influencer.setRole(Role.Influencer);
			influencer.setInfluencer(null);
			influencer = userDao.save(influencer);
			inf.setUser(null);
			inf.setInfluencerId(influencer.getUserId());
			influencer.setInfluencer(influencerDao.save(inf));

			adminToken = authenticationService.generateTokenFromUser(admin, this.request.getRemoteAddr()).getToken();
			influencerToken = authenticationService.generateTokenFromUser(influencer, this.request.getRemoteAddr()).getToken();
			brandToken = authenticationService.generateTokenFromUser(brand, this.request.getRemoteAddr()).getToken();

			this.admin = admin;
			this.brand = brand;
			this.influencer = influencer;

		} catch(ConstraintViolationException e) {
			System.err.println(e.getConstraintName());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	@After
	public void after() {
	}
}
