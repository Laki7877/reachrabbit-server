package com.ahancer.rr.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.utils.CacheUtil;
import com.ahancer.rr.utils.EncryptionUtil;
import com.ahancer.rr.utils.JwtUtil;

@Service
@Transactional(rollbackFor=Exception.class)
public class AuthenticationService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private EncryptionUtil encrypt;
	
	@Autowired
	private JwtUtil jwt;
	
	@Value("${reachrabbit.cache.userrequest}")
	private String userRequestCache;

	public AuthenticationResponse brandAuthentication(String email, String password) {
		User user = userDao.findByEmail(email);
		if(null == user || !encrypt.checkPassword(password, user.getPassword()) || user.getRole() != Role.Brand){
			return null;
		} else {
			String token = jwt.generateToken(user.getUserId());
			CacheUtil.putCacheObject(userRequestCache, token, user);
			AuthenticationResponse response = new AuthenticationResponse(token);
			return response;
		}
	}
	
	public AuthenticationResponse influencerAuthentication(String socialId, String providerName) {
		User user = userDao.findByInfluencerMediaLinksMediaMediaIdAndInfluencerMediaLinksSocialId(providerName, socialId);
		if(user == null) {
			return null;
		}
		
		String token = jwt.generateToken(user.getUserId());
		CacheUtil.putCacheObject(userRequestCache, token, user);
		AuthenticationResponse response = new AuthenticationResponse(token);
		return response;
	}
	
	public AuthenticationResponse generateTokenFromUser(User user) {
		String token = jwt.generateToken(user.getUserId());
		CacheUtil.putCacheObject(userRequestCache, token, user);
		AuthenticationResponse response = new AuthenticationResponse(token);
		return response;
	}

	public User getUserById(Long userId) {
		return userDao.findOne(userId);
	}

}
