package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.google.api.client.util.Lists;

@Service
@Transactional(rollbackFor=Exception.class)
public class InfluencerService {
	@Autowired
	private InfluencerDao influencerDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private InfluencerMediaDao influencerMediaDao;
	
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
		user.setInfluencer(influencerDao.save(influencer));
		for(InfluencerMedia link : influencer.getInfluencerMedias()) {
			link.setInfluencerMediaId(new InfluencerMediaId(influencer.getInfluencerId(), link.getMedia().getMediaId()));
		}
		influencerMediaDao.save(influencer.getInfluencerMedias());
		return user;
	}
}
