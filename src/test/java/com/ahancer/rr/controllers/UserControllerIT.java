package com.ahancer.rr.controllers;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

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
public class UserControllerIT extends AbstractControllerIT {
}
