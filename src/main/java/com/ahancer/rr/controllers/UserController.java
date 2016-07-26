package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.User;


@Controller
@RequestMapping("/users")
public class UserController extends AbstractController {

	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value="/{userId}",method=RequestMethod.GET)
	@ResponseBody
	public User GetOneUser(@PathVariable long userId) {
		return userDao.findOne(userId);
	}

}
