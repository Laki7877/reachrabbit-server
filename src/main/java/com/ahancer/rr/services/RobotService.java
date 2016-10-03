package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.User;

@Service
public class RobotService {
	
	private User robotUser;
	
	@Autowired
	private UserDao userDao;
	
	public RobotService(){
		robotUser = new User();
		robotUser.setUserId(1L);
		robotUser.setRole(Role.Bot);
	}
	
	public User getRobotUser() {
		return userDao.findOne(1L);
	}
	
}
