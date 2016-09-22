package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Resource;
import com.ahancer.rr.request.ResourceRemoteRequest;
import com.ahancer.rr.services.ResourceService;

@RestController
@RequestMapping("/resources")
public class ResourceController {
	@Autowired
	private ResourceService resourceService;
	@RequestMapping(method=RequestMethod.POST)
	public Resource createResource(@RequestBody MultipartFile file) throws Exception{
		return resourceService.upload(file);
	}
	@RequestMapping(value="/remote",method=RequestMethod.POST)
	public Resource createResource(@RequestBody ResourceRemoteRequest request) throws Exception {
		return resourceService.upload(request);
	}
	@RequestMapping(value="/{resourceId}",method=RequestMethod.GET)
	public Resource getOneResource(@PathVariable Long resourceId) throws Exception{
		throw new ResponseException();
	}
	@RequestMapping(value="/{resourceId}",method=RequestMethod.PUT)
	public Resource updateResource(@PathVariable Long resourceId,@RequestBody Resource resource) throws Exception{
		throw new ResponseException("error.notimplement");
	}
	@RequestMapping(value="/{resourceId}",method=RequestMethod.DELETE)
	public void deleteResource(@PathVariable Long resourceId) throws Exception{
		throw new ResponseException("error.notimplement");
	}

}
