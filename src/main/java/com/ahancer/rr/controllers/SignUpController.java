package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.models.User;
import com.ahancer.rr.request.BrandSignUpRequest;
import com.ahancer.rr.request.InfluencerSignUpRequest;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.services.AuthenticationService;
import com.ahancer.rr.services.BrandService;
import com.ahancer.rr.services.InfluencerService;

@RestController
@RequestMapping("/signup")
public class SignUpController {
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private InfluencerService influencerService;
	
	@RequestMapping(value="/brand", method=RequestMethod.POST)
	public AuthenticationResponse signUpBrand(@RequestBody BrandSignUpRequest request) throws Exception {
		User newUser = brandService.signUpBrand(request);
		return authenticationService.generateTokenFromUser(newUser);
	}
	@RequestMapping(value="/influencer", method=RequestMethod.POST)
	public AuthenticationResponse signUpInfluencer(@RequestBody InfluencerSignUpRequest request) throws Exception {
		User newUser = influencerService.signupInfluencer(request);
		return authenticationService.generateTokenFromUser(newUser);
	}

}
