package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.models.Brand;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.services.BrandService;

@RestController
@RequestMapping("/signup")
public class SignUpController {
	
	@Autowired
	private BrandService brandService;
	
	@RequestMapping(method=RequestMethod.POST)
	public AuthenticationResponse SignUpBrand(@RequestBody Brand brand) throws Exception {
		String token = brandService.signUpBrand(brand);
		AuthenticationResponse response = new AuthenticationResponse(token);
		return response;
	}

}
