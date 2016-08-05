package com.ahancer.rr.controllers;
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
	
	public void findUser() {
	}
}
