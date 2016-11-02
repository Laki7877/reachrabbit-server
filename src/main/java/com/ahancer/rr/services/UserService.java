package com.ahancer.rr.services;

import org.springframework.stereotype.Service;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.UserResponse;

@Service
public interface UserService {
	public User findOne(Long userId) throws Exception;
	public UserResponse findUserById(Long requestUserId,Long userId, Role userRole) throws Exception;
}
