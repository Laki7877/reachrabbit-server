package com.ahancer.rr.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.services.UserService;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProposalDao proposalDao;
	
	public User findOne(Long userId) throws Exception {
		return userDao.findOne(userId);
	}
	
	public UserResponse findUserById(Long requestUserId,Long userId, Role userRole) throws Exception {
		Long proposalCount = 0L;
		if(Role.Brand.equals(userRole)){
			proposalCount = proposalDao.countByCampaignBrandIdAndInfluencerId(requestUserId, userId);
		} else if(Role.Influencer.equals(userRole)) {
			proposalCount = proposalDao.countByCampaignBrandIdAndInfluencerId(userId, requestUserId);
		} else if(Role.Admin.equals(userRole)){
			proposalCount = 1L;
		}
		if(0L >= proposalCount){
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.user.not.found");
		}
		User user = userDao.findOne(userId);
		if(null == user 
				|| Role.Admin.equals(user.getRole())
				|| Role.Bot.equals(user.getRole())
				|| userRole.equals(user.getRole())) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.user.not.found");
		}
		return new UserResponse(user,userRole.displayName());
	}
	

}
