package com.ahancer.rr.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.response.UserResponse;

@Component
public abstract class AbstractController {
	
	@Autowired
	private HttpServletRequest request;
	
	final public UserResponse getUserRequest() { 
		Object user = request.getAttribute(ApplicationConstant.UserRequest);
		if(user instanceof UserResponse){
			return (UserResponse)user;
		} else {
			return null;
		}
	}
	
	final public String getTokenRequest() {
		Object token = request.getAttribute(ApplicationConstant.TokenAttribute);
		if(null != token){
			return (String)token;
		}
		return null;
	}
}
