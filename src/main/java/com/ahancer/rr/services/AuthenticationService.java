package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.User;
import com.ahancer.rr.utils.EncryptionUtil;


@Component
public class AuthenticationService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private EncryptionUtil encrypt;
	
	
	public User brandAuthentication(String email, String password) {
		User user = userDao.findByEmail(email);
		if(null == user || !encrypt.checkPassword(password, user.getPassword()) || user.getRole() != Role.Brand){
			return null;
		} else {
			return user;
		}
	}
	
	public User getUserById(Long userId) {
		return userDao.findOne(userId);
	}
	
}
