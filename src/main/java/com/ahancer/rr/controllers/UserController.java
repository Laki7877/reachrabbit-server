package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.User;


@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {

	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value="/{userId}",method=RequestMethod.GET)
	@Authorization(Role.Admin)
	public User GetOneUser(@PathVariable long userId) {
		return userDao.findOne(userId);
	}

}
