package com.ahancer.rr.services;

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
import com.ahancer.rr.utils.Util;

@Service
@Transactional(rollbackFor=Exception.class)
public class InfluencerService {
	@Autowired
	private InfluencerDao influencerDao;
	
	@Value("${reachrabbit.cache.userrequest")
	private String userRequestCache;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private InfluencerMediaDao influencerMediaDao;
	
	public User updateInfluencerUser(Long userId, User newUser, String token) throws ResponseException {
		User oldUser = userDao.findOne(userId);
		if(oldUser == null) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.not.found");
		}
		for(InfluencerMedia link : newUser.getInfluencer().getInfluencerMedias()) {
			InfluencerMediaId id = new InfluencerMediaId(userId, link.getMedia().getMediaId());
			link.setInfluencerMediaId(id);
		}
		Util.copyProperties(newUser, oldUser);
		User user = userDao.save(oldUser);
		//CacheUtil.updateCacheObject(userRequestCache, token, user);
		return user;
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
//		
//		//Check if media link exists
		for(InfluencerMedia link : influencer.getInfluencerMedias()) {
			if(influencerMediaDao.countByMediaIdAndSocialId(link.getMedia().getMediaId(), link.getSocialId()) > 0) {
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.media.already.exist");
			}
		}
		
		user.setRole(Role.Influencer);
		user.setInfluencer(null);
		userDao.save(user);
		influencer.setInfluencerId(user.getUserId());
		for(InfluencerMedia link : influencer.getInfluencerMedias()) {
			link.setInfluencer(influencer);
			link.setInfluencerMediaId(new InfluencerMediaId(influencer.getInfluencerId(), link.getMedia().getMediaId()));
		}
		user.setInfluencer(influencerDao.save(influencer));
		//influencerMediaDao.save(influencer.getInfluencerMedias());
		return user;
	}

}
