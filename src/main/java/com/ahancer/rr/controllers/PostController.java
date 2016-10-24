package com.ahancer.rr.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.services.PostService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/posts")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@ApiOperation(value = "Adhoc run post schedule")
	@RequestMapping(method=RequestMethod.POST)
	@Authorization({Role.Admin})
	public void runAdhocSchedule(@RequestBody(required=true) Date dataDate){
		postService.createPostSchedule(dataDate);
	}

}
