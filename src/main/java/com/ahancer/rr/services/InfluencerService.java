package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.InfluencerDao;
import com.ahancer.rr.daos.MediaLinkDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Influencer;
import com.ahancer.rr.models.MediaLink;
import com.ahancer.rr.models.User;

@Service
@Transactional(rollbackFor=Exception.class)
public class InfluencerService {
	@Autowired
	private InfluencerDao influencerDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private MediaLinkDao mediaLinkDao;
	
	public User signupInfluencer(User user) throws ResponseException {
		Influencer influencer = user.getInfluencer();
		
		//Check for influencer object
		if(null == influencer) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.signup.no.influencer");
		}
		
		//Check for social media linkage
		if(influencer.getMediaLink() == null || 
			influencer.getMediaLink().size() == 0) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.signup.no.media");
		}
		
		//Check if media link exists
		for(MediaLink link : influencer.getMediaLink()) {
			if(mediaLinkDao.countByMediaIdAndSocialId(link.getMediaId(), link.getSocialId()) > 0) {
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.signup.alreadyexist.media");
			}
		}
		
		user.setRole(Role.Influencer);
		user.setInfluencer(null);
		userDao.save(user);
		influencer.setInfluencerId(user.getUserId());
		user.setInfluencer(influencerDao.save(influencer));
		
		return user;
	}
}
