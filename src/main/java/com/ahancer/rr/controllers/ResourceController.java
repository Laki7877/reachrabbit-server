package com.ahancer.rr.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Resource;

@RestController
@RequestMapping("/resources")
public class ResourceController {
	
	@RequestMapping(value="/{resourcId}",method=RequestMethod.GET)
	public Resource GetOneResource(@PathVariable Long resourcId) throws Exception{
		throw new ResponseException();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Resource CreateResource(@RequestBody Resource resource) throws Exception{
		throw new ResponseException();
	}
	
	@RequestMapping(value="/{resourcId}",method=RequestMethod.PUT)
	public Resource UpdateResource(@PathVariable Long resourcId,@RequestBody Resource resource) throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
	@RequestMapping(value="/{resourcId}",method=RequestMethod.DELETE)
	public void DeleteResource(@PathVariable Long resourcId) throws Exception{
		throw new ResponseException("error.notimplement");
	}

}
