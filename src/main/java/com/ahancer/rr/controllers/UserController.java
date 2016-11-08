package com.ahancer.rr.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.services.AuthenticationService;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private UserDao userDao;
	@ApiOperation(value = "Get user from user id")
	@RequestMapping(value="/{userId}",method=RequestMethod.GET)
	@Authorization({Role.Admin})
	public User getOneUser(@PathVariable long userId) {
		return userDao.findOne(userId);
	}
	@ApiOperation(value = "Get all brand pagenation")
	@RequestMapping(value="/brand", method=RequestMethod.GET)
	@Authorization(Role.Admin)
	public Page<UserResponse> getBrands(@RequestParam(required=false, name="search") String search, Pageable pageable) {
		if(search == null) {
			return userDao.findAllBrand(pageable);
		}
		return userDao.findAllBrand(search, pageable);
	}
	@ApiOperation(value = "Get all influencer pagenation")
	@RequestMapping(value="/influencer", method=RequestMethod.GET)
	@Authorization(Role.Admin)
	public Page<UserResponse> getInfluencers(@RequestParam(required=false, name="search") String search, Pageable pageable) {
		if(search == null) {
			return userDao.findAllInfluencer(pageable);
		}
		return userDao.findAllInfluencer(search, pageable);
	}
	
	@ApiOperation(value = "Get login as token")
	@RequestMapping(value="/{userId}/loginas", method=RequestMethod.GET)
	@Authorization(Role.Admin)
	public AuthenticationResponse getInfluencers(@PathVariable long userId) throws Exception {
		AuthenticationResponse authen = authenticationService.adminLoginAs(userId, request.getRemoteAddr());
		return authen;
	}
}
