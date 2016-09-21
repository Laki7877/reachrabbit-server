package com.ahancer.rr.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.utils.CacheUtil;
import com.ahancer.rr.utils.EncryptionUtil;
import com.ahancer.rr.utils.JwtUtil;
import com.ahancer.rr.utils.Util;

@Service
@Transactional(rollbackFor=Exception.class)
public class AuthenticationService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private EncryptionUtil encrypt;
	
	@Autowired
	private JwtUtil jwt;

	public AuthenticationResponse brandAuthentication(String email, String password) {
		User user = userDao.findByEmail(email);
		if(null == user || !encrypt.checkPassword(password, user.getPassword()) || user.getRole() != Role.Brand){
			return null;
		} else {
			String token = jwt.generateToken(user.getUserId());
			UserResponse userResponse = Util.getUserResponse(user);
			CacheUtil.putCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
			AuthenticationResponse response = new AuthenticationResponse(token);
			return response;
		}
	}
	public AuthenticationResponse adminAuthentication(String email, String password) {
		User user = userDao.findByEmail(email);
		if(null == user || !encrypt.checkPassword(password, user.getPassword()) || user.getRole() != Role.Admin){
			return null;
		} else {
			String token = jwt.generateToken(user.getUserId());
			UserResponse userResponse = Util.getUserResponse(user);
			CacheUtil.putCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
			AuthenticationResponse response = new AuthenticationResponse(token);
			return response;
		}
	}
	public AuthenticationResponse influencerAuthentication(String socialId, String providerName) {
		User user = userDao.findBySocialIdAndMediaId(providerName, socialId);
		if(user == null) {
			return null;
		}
		String token = jwt.generateToken(user.getUserId());
		UserResponse userResponse = Util.getUserResponse(user);
		CacheUtil.putCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
		AuthenticationResponse response = new AuthenticationResponse(token);
		return response;
	}
	
	public AuthenticationResponse generateTokenFromUser(User user) {
		String token = jwt.generateToken(user.getUserId());
		UserResponse userResponse = Util.getUserResponse(user);
		CacheUtil.putCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
		AuthenticationResponse response = new AuthenticationResponse(token);
		return response;
	}

	public User getUserById(Long userId) {
		return userDao.findOne(userId);
	}
	
	public AuthenticationResponse influencerEmailAuthentication(String email, String password) {
		User user = userDao.findByEmail(email);
		if(null == user || !encrypt.checkPassword(password, user.getPassword()) || user.getRole() != Role.Influencer){
			return null;
		} else {
			String token = jwt.generateToken(user.getUserId());
			UserResponse userResponse = Util.getUserResponse(user);
			CacheUtil.putCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
			AuthenticationResponse response = new AuthenticationResponse(token);
			return response;
		}
	}

}
