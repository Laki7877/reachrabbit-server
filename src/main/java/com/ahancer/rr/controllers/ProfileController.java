package com.ahancer.rr.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.models.User;

@RestController
@RequestMapping("/profile")
public class ProfileController extends AbstractController{
	
	
	@RequestMapping(method=RequestMethod.GET)
	public User getMyProfile() {
		return this.getUserRequest();
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public User updateProfile(@RequestBody User user) {
		return null;
	}
}
