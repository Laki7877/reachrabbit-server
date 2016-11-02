package com.ahancer.rr.services;

import org.springframework.stereotype.Service;

import com.ahancer.rr.models.User;

@Service
public interface RobotService {
	public User getRobotUser() throws Exception;
}
