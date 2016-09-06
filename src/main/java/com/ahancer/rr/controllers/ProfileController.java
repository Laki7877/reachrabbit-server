package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.request.PayoutRequest;
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
	public UserResponse getMyProfile() {
		return this.getUserRequest();
	}
	
	@RequestMapping(value="/bank",method=RequestMethod.PUT)
	@Authorization(Role.Influencer)
	public UserResponse updateBank(@RequestBody PayoutRequest request) throws Exception {
		return influencerService.updateBankDetail(request, this.getUserRequest().getInfluencer().getInfluencerId(), this.getTokenRequest());
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
	public UserResponse getProfile(@PathVariable Long userId) throws Exception {
		return userService.findUserById(this.getUserRequest().getUserId(),userId,this.getUserRequest().getRole());
	}
	
	



}
