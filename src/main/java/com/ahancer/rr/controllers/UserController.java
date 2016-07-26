package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.User;


@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserDao userDao;

	@RequestMapping(value="/create",method=RequestMethod.POST)
	@ResponseBody
	public String CreateUser(@RequestBody User user) {
		
		try {
			userDao.save(user);
		}
		catch (Exception ex) {
			return "Error creating the user: " + ex.toString();
		}
		return "User succesfully created! (id = " + user.getUserId() + ")";
	}
	
	@RequestMapping(value="/{userId}",method=RequestMethod.GET)
	@ResponseBody
	public User GetOneUser(@PathVariable long userId) {
		try {
			return userDao.findOne(userId);
		}
		catch (Exception ex) {
			return null;
		}
		
	}

}
