package com.ahancer.rr.services.impl;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.response.TokenResponse;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.services.AuthenticationService;
import com.ahancer.rr.utils.CacheUtil;
import com.ahancer.rr.utils.EncodeUtil;
import com.ahancer.rr.utils.EncryptionUtil;
import com.ahancer.rr.utils.Util;

@Component
@Transactional(rollbackFor=Exception.class)
public class AuthenticationServiceImpl implements AuthenticationService {
	@Autowired
	private CacheUtil cacheUtil;
	@Autowired
	private UserDao userDao;
	@Autowired
	private EncryptionUtil encrypt;

	public AuthenticationResponse brandAuthentication(String email, String password, String ip) throws Exception {
		User user = userDao.findByEmailAndRole(email,Role.Brand);
		if(null == user || !encrypt.checkPassword(password, user.getPassword())){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.unauthorize");
		} else {
			TokenResponse tokenObject = new TokenResponse(user.getUserId(),ip,new Date());
			String token = EncodeUtil.encodeObject(tokenObject);
			UserResponse userResponse = Util.getUserResponse(user);
			cacheUtil.putCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
			AuthenticationResponse response = new AuthenticationResponse(token);
			return response;
		}
	}
	
	public AuthenticationResponse adminAuthentication(String email, String password, String ip) throws Exception {
		User user = userDao.findByEmailAndRole(email,Role.Admin);
		if(null == user || !encrypt.checkPassword(password, user.getPassword())){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.unauthorize");
		} else {
			TokenResponse tokenObject = new TokenResponse(user.getUserId(),ip,new Date());
			String token = EncodeUtil.encodeObject(tokenObject);
			UserResponse userResponse = Util.getUserResponse(user);
			cacheUtil.putCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
			AuthenticationResponse response = new AuthenticationResponse(token);
			return response;
		}
	}
	
	public AuthenticationResponse influencerAuthentication(String socialId, String providerName, String ip) throws Exception {
		User user = userDao.findBySocialIdAndMediaId(providerName, socialId);
		if(user == null) {
			return null;
		}
		TokenResponse tokenObject = new TokenResponse(user.getUserId(),ip,new Date());
		String token = EncodeUtil.encodeObject(tokenObject);
		UserResponse userResponse = Util.getUserResponse(user);
		cacheUtil.putCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
		AuthenticationResponse response = new AuthenticationResponse(token);
		return response;
	}
	
	public AuthenticationResponse generateTokenFromUser(User user, String ip) throws Exception {
		TokenResponse tokenObject = new TokenResponse(user.getUserId(),ip,new Date());
		String token = EncodeUtil.encodeObject(tokenObject);
		UserResponse userResponse = Util.getUserResponse(user);
		cacheUtil.putCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
		AuthenticationResponse response = new AuthenticationResponse(token);
		return response;
	}

	public User getUserById(Long userId) throws Exception {
		return userDao.findOne(userId);
	}
	
	public AuthenticationResponse influencerEmailAuthentication(String email, String password, String ip) throws Exception  {
		User user = userDao.findByEmailAndRole(email,Role.Influencer);
		if(null == user || !encrypt.checkPassword(password, user.getPassword()) ){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.unauthorize");
		} else {
			TokenResponse tokenObject = new TokenResponse(user.getUserId(),ip,new Date());
			String token = EncodeUtil.encodeObject(tokenObject);
			UserResponse userResponse = Util.getUserResponse(user);
			cacheUtil.putCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
			AuthenticationResponse response = new AuthenticationResponse(token);
			return response;
		}
	}
	
	public AuthenticationResponse adminLoginAs(long userId, String ip ) throws Exception {
		User user = userDao.findOne(userId);
		if(null == user){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.unauthorize");
		} else {
			TokenResponse tokenObject = new TokenResponse(user.getUserId(),ip,new Date());
			String token = EncodeUtil.encodeObject(tokenObject);
			UserResponse userResponse = Util.getUserResponse(user);
			cacheUtil.putCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
			AuthenticationResponse response = new AuthenticationResponse(token);
			return response;
		}
	}

}
