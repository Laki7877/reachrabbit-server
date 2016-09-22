package com.ahancer.rr.controllers;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/signup")
public class SignUpController {
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private InfluencerService influencerService;
	@ApiOperation(value = "Sign up by brand")
	@RequestMapping(value="/brand", method=RequestMethod.POST)
	public AuthenticationResponse signUpBrand(@Valid @RequestBody BrandSignUpRequest request
			,@RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		User newUser = brandService.signUpBrand(request,locale);
		return authenticationService.generateTokenFromUser(newUser);
	}
	@ApiOperation(value = "Sign up by influencer")
	@RequestMapping(value="/influencer", method=RequestMethod.POST)
	public AuthenticationResponse signUpInfluencer(@Valid @RequestBody InfluencerSignUpRequest request
			,@RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		User newUser = influencerService.signupInfluencer(request,locale);
		return authenticationService.generateTokenFromUser(newUser);
	}

}
