package com.ahancer.rr.controllers;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.User;
import com.ahancer.rr.utils.EncryptionUtil;

public abstract class AbstractIT {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private EncryptionUtil encryptionUtil;
	
	@Before
	public void before() {
		
		//Create admin
		User admin = new User();
		admin.setName("PLK THE ADMIN");
		admin.setEmail("admin@reachrabbit.com");
		admin.setPassword(encryptionUtil.hashPassword("1234"));
		admin.setRole(Role.Admin);
		
		userDao.save(Arrays.asList(admin));
	}
	
	@After
	public void after() {
		userDao.deleteAll();
	}
}
