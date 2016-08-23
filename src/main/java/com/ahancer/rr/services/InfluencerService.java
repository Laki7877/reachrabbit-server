package com.ahancer.rr.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.InfluencerDao;
import com.ahancer.rr.daos.InfluencerMediaDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Influencer;
import com.ahancer.rr.models.InfluencerMedia;
import com.ahancer.rr.models.InfluencerMediaId;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.utils.CacheUtil;
import com.ahancer.rr.utils.Util;

@Service
@Transactional(rollbackFor=Exception.class)
public class InfluencerService {
	@Autowired
	private InfluencerDao influencerDao;
	
	@Value("${reachrabbit.cache.userrequest}")
	private String userRequestCache;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private InfluencerMediaDao influencerMediaDao;
	
	public UserResponse updateInfluencerUser(Long userId, User newUser, String token) throws ResponseException {
		User oldUser = userDao.findOne(userId);
		if(oldUser == null) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.not.found");
		}
		for(InfluencerMedia link : newUser.getInfluencer().getInfluencerMedias()) {
			InfluencerMediaId id = new InfluencerMediaId(userId, link.getMedia().getMediaId());
			link.setInfluencerMediaId(id);
		}
		//Validate duplicate Email
		if(StringUtils.isNotEmpty(oldUser.getEmail()) 
				&& !oldUser.getEmail().equals(newUser.getEmail())) {
			int countEmail = userDao.countByEmail(newUser.getEmail());
			if(0 < countEmail){
				throw new ResponseException(HttpStatus.BAD_REQUEST,"error.email.duplicate");
			}
		}
		Util.copyProperties(newUser, oldUser);
		oldUser.setProfilePicture(newUser.getProfilePicture());
		User user = userDao.save(oldUser);
		UserResponse userResponse = Util.getUserResponse(user);
		CacheUtil.updateCacheObject(userRequestCache, token, userResponse);
		return userResponse;
	}
	
	public User signupInfluencer(User user) throws ResponseException {
		Influencer influencer = user.getInfluencer();
		//Check for influencer object
		if(null == influencer) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.signup.no.influencer");
		}
		//Check for social media linkage
		if(influencer.getInfluencerMedias() == null || 
				influencer.getInfluencerMedias().size() == 0) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.signup.no.media");
		}
		//Check if media link exists
		for(InfluencerMedia link : influencer.getInfluencerMedias()) {
			if(influencerMediaDao.countByMediaIdAndSocialId(link.getMedia().getMediaId(), link.getSocialId()) > 0) {
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.media.already.exist");
			}
		}
		//Validate duplicate Email
		int countEmail = userDao.countByEmail(user.getEmail());
		if(0 < countEmail){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.email.duplicate");
		}
		user.setRole(Role.Influencer);
		user.setInfluencer(null);
		userDao.save(user);
		influencer.setUser(null);
		influencer.setInfluencerId(user.getUserId());
		for(InfluencerMedia link : influencer.getInfluencerMedias()) {
			link.setInfluencer(influencer);
			link.setInfluencerMediaId(new InfluencerMediaId(influencer.getInfluencerId(), link.getMedia().getMediaId()));
		}
		user.setInfluencer(influencerDao.save(influencer));
		return user;
	}

}
