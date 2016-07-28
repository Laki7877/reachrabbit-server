package com.ahancer.rr.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Bank;
import com.ahancer.rr.models.Category;
import com.ahancer.rr.models.Media;

@RestController
@RequestMapping("/data")
public class DataController {

	@RequestMapping(value="/media",method=RequestMethod.GET)
	public List<Media> GetAllMedia() throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
	@RequestMapping(value="/categories",method=RequestMethod.GET)
	public List<Category> GetAllCategories() throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
	@RequestMapping(value="/banks",method=RequestMethod.GET)
	public List<Bank> GetAllBanks() throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
}
