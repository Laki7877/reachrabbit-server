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
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.services.BrandService;

@RestController
@RequestMapping("/users/brands")
public class BrandController {
	
	@Autowired
	private BrandService brandService;

	@RequestMapping(method=RequestMethod.GET)
	public List<Brand> getAllBrand() throws Exception{
		throw new ResponseException();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public AuthenticationResponse createBrand(@RequestBody Brand brand) throws ResponseException {
		AuthenticationResponse authenticationResponse = new AuthenticationResponse(brandService.signUpBrand(brand));
		return authenticationResponse;
	}
	
	@RequestMapping(value="/{brandId}",method=RequestMethod.GET)
	public Brand getOneBrand(@PathVariable Long brandId) throws Exception {
		Brand brand = brandService.getBrand(brandId);
		return brand;
	}
	
	@RequestMapping(value="/{brandId}",method=RequestMethod.PUT)
	public Brand updateBrand(@PathVariable Long brandId,@RequestBody Brand brand) throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
	@RequestMapping(value="/{brandId}",method=RequestMethod.DELETE)
	public void deleteBrand(@PathVariable Long brandId) throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
}
