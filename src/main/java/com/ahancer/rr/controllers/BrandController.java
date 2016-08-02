package com.ahancer.rr.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.request.AuthenticationRequest;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.services.BrandService;

@RestController
@RequestMapping("/users/brands")
public class BrandController {
	
	@Autowired
	private BrandService brandService;

	@RequestMapping(method=RequestMethod.GET)
	public List<Brand> GetAllBrand() throws Exception{
		throw new ResponseException();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public AuthenticationResponse CreateBrand(@RequestBody Brand brand) throws ResponseException {
		AuthenticationResponse authenticationResponse = new AuthenticationResponse(brandService.signUpBrand(brand));
		return authenticationResponse;
	}
	
	@RequestMapping(value="/{brandId}",method=RequestMethod.GET)
	public Brand GetOneBrand(@PathVariable Long brandId) throws Exception{
		throw new ResponseException();
	}
	
	@RequestMapping(value="/{brandId}",method=RequestMethod.PUT)
	public Brand UpdateBrand(@PathVariable Long brandId,@RequestBody Brand brand) throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
	@RequestMapping(value="/{brandId}",method=RequestMethod.DELETE)
	public void DeleteBrand(@PathVariable Long brandId) throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
}
