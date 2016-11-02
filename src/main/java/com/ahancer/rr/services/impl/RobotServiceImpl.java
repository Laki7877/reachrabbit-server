package com.ahancer.rr.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.User;
import com.ahancer.rr.services.RobotService;

@Component
@Transactional(rollbackFor=Exception.class)
public class RobotServiceImpl implements RobotService {
	
	private User robotUser;
	
	@Autowired
	private UserDao userDao;
	
	public RobotServiceImpl() {
		robotUser = new User();
		robotUser.setUserId(1L);
		robotUser.setRole(Role.Bot);
	}
	
	public User getRobotUser() throws Exception {
		return userDao.findOne(1L);
	}
	
}
