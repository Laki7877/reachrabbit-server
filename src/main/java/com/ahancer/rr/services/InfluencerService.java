package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.CategoryDao;
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

	@Autowired
	private CategoryDao categoryDao;
	public User updateInfluencerUser(Long userId, User newUser, String token) throws ResponseException {
		User oldUser = userDao.findOne(userId);
		if(oldUser == null) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.not.found");
		}
		/*
		List<InfluencerMedia> newList = new ArrayList<InfluencerMedia>();
		//Override duplicates
		for(InfluencerMedia link : oldUser.getInfluencer().getInfluencerMedias()) {
			boolean isFound = false;
			for(InfluencerMedia link2 : newUser.getInfluencer().getInfluencerMedias()) {
				if(link2.getInfluencerMediaId().getMediaId().equals(link.getInfluencerMediaId().getMediaId())) {
					newList.add(link2);
					isFound = true;
				}
			}
			if(!isFound) {
				newList.add(link);
			}
		}
		
		//Override categories
		List<Category> newCategory = new ArrayList<Category>();
		for(Category cat : oldUser.getInfluencer().getCategories()) {
			boolean isFound = false;
			for(Category cat2 : newUser.getInfluencer().getCategories()) {
				if(cat.getCategoryId().equals(cat2.getCategoryId())) {
					newCategory.add(cat2);
					isFound = true;
				}
			}
			if(!isFound) {
				newCategory.add(cat);
			}
		}*/
		
		Util.copyProperties(newUser, oldUser);/*
		oldUser.getInfluencer().setInfluencerMedias(newList);
		oldUser.getInfluencer().setCategories(newCategory);*/
		influencerMediaDao.save(oldUser.getInfluencer().getInfluencerMedias());
		categoryDao.save(oldUser.getInfluencer().getCategories());
		User user = userDao.save(oldUser);
	//	CacheUtil.updateCacheObject(userRequestCache, token, user);
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
		user.setInfluencer(influencerDao.save(influencer));
		for(InfluencerMedia link : influencer.getInfluencerMedias()) {
			link.setInfluencerMediaId(new InfluencerMediaId(influencer.getInfluencerId(), link.getMedia().getMediaId()));
		}
		influencerMediaDao.save(influencer.getInfluencerMedias());
		return user;
	}
}
