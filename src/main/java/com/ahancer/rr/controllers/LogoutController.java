package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.services.AuthenticationService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/logout")
public class LogoutController extends AbstractController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@ApiOperation(value = "Logout")
	@RequestMapping(method = RequestMethod.GET)
	public void logout() throws Exception {
		authenticationService.logout(this.getTokenRequest());
	}

}
