package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.User;
import com.ahancer.rr.request.ProfileRequest;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.services.BrandService;
import com.ahancer.rr.services.InfluencerService;
import com.ahancer.rr.services.UserService;
import com.mysql.jdbc.NotImplemented;

@RestController
@RequestMapping("/profile")
public class ProfileController extends AbstractController{

	@Autowired
	private BrandService brandService;

	@Autowired
	private InfluencerService influencerService;

	@Autowired
	private UserService userService;

	@RequestMapping(method=RequestMethod.GET)
	public User getMyProfile() {
		return userService.findUserById(this.getUserRequest().getUserId());
	}

	@RequestMapping(method=RequestMethod.PUT)
	public UserResponse updateProfile(@RequestBody ProfileRequest request) throws Exception {
		UserResponse ownUser = this.getUserRequest();
		if(ownUser.getRole().equals(Role.Brand)) {
			return brandService.updateBrandUser(ownUser.getUserId(), request, this.getTokenRequest());
		} else if(ownUser.getRole().equals(Role.Influencer)) {
			return influencerService.updateInfluencerUser(ownUser.getUserId(), request, this.getTokenRequest());
		}
		throw new NotImplemented();
	}

	@RequestMapping(value="/{userId}",method=RequestMethod.GET)
	public User getProfile(@PathVariable Long userId) throws Exception {
		return userService.findUserById(this.getUserRequest().getUserId(),userId,this.getUserRequest().getRole());
	}



}
