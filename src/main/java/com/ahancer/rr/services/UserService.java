package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.utils.Util;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	
	public UserResponse findUserById(Long userId, Role userRole) throws Exception {
		User user = userDao.findOne(userId);
		if(null == user 
				|| Role.Admin.equals(user.getRole())
				|| Role.Bot.equals(user.getRole())
				|| userRole.equals(user.getRole())){
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.brand.not.found");
		}
		return Util.getUserResponse(user);
	}
	

}
