package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.models.User;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.services.BrandService;

@RestController
@RequestMapping("/signup")
public class SignUpController {
	
	@Autowired
	private BrandService brandService;
	
	@RequestMapping(value="/brand", method=RequestMethod.POST)
	public AuthenticationResponse signUpBrand(@RequestBody User user) throws Exception {
		String token = brandService.signUpBrand(user);
		AuthenticationResponse response = new AuthenticationResponse(token);
		return response;
	}

}
