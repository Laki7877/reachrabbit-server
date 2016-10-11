package com.ahancer.rr.controllers;

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

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {
	@Autowired
	private UserDao userDao;
	@ApiOperation(value = "Get user from user id")
	@RequestMapping(value="/{userId}",method=RequestMethod.GET)
	@Authorization({Role.Admin})
	public User getOneUser(@PathVariable long userId) {
		return userDao.findOne(userId);
	}
	
	@RequestMapping(value="/brand", method=RequestMethod.GET)
	@Authorization(Role.Admin)
	public Page<User> getBrands(@RequestParam(required=false, name="search") String search, Pageable pageable) {
		if(search == null) {
			return userDao.findByRole(Role.Brand, pageable);
		}
		return userDao.findByRoleAndNameOrBrandBrandName(Role.Brand, search, search, pageable);
	}
	@RequestMapping(value="/influencer", method=RequestMethod.GET)
	@Authorization(Role.Admin)
	public Page<User> getInfluencers(@RequestParam(required=false, name="search") String search, Pageable pageable) {
		if(search == null) {
			return userDao.findByRole(Role.Influencer, pageable);
		}
		return userDao.findByRoleAndName(Role.Influencer, search, pageable);
	}
}
