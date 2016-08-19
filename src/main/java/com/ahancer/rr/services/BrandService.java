package com.ahancer.rr.services;



import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.CampaignStatus;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.BrandDao;
import com.ahancer.rr.daos.CampaignDao;
import com.ahancer.rr.daos.MediaDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.Media;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.utils.CacheUtil;
import com.ahancer.rr.utils.EncryptionUtil;
import com.ahancer.rr.utils.Util;

@Service
@Transactional(rollbackFor=Exception.class)
public class BrandService {


	@Autowired
	private BrandDao brandDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CampaignDao campaignDao;
	
	@Autowired
	private MediaDao mediaDao;

	@Autowired
	private EncryptionUtil encrypt;

	
	@Value("${reachrabbit.cache.userrequest}")
	private String userRequestCache;

	public User signUpBrand(User user) throws ResponseException {
		Brand brand = user.getBrand();
		if(null == brand){
			brand = new Brand();
		}
		//Validate duplicate Email
		int emailCount = userDao.countByEmail(user.getEmail());
		if(emailCount > 0) {
			throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"error.email.duplicate");
		}
		String hashPassword = encrypt.hashPassword(user.getPassword());
		user.setPassword(hashPassword);
		user.setRole(Role.Brand);
		user.setBrand(null);
		userDao.save(user);
		brand.setUser(null);
		brand.setBrandId(user.getUserId());
		brand = brandDao.save(brand);
		user.setBrand(brand);
		//Create campaign
		Campaign campaign = new Campaign();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 5);
		campaign.setSubmissionDeadline(cal.getTime());
		campaign.setFromBudget(null);
		campaign.setToBudget(null);
		campaign.setBrandId(brand.getBrandId());
		campaign.setTitle("Campaign แรกของคุณ");
		campaign.setCategory(null);
		campaign.setDescription("นี่คือคำอธิบาย");
		campaign.setStatus(CampaignStatus.Draft);
		Set<Media> allMedia = new HashSet<Media>();
		mediaDao.findAll().forEach(allMedia::add);
		campaign.setMedia(allMedia);	
		campaignDao.save(campaign);
		return user;
	}
	
	public UserResponse updateBrandUser(Long userId, User newUser,String token) throws ResponseException {
		User oldUser = userDao.findOne(userId);
		if(null == oldUser) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.brand.not.found");
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
		if(StringUtils.isNotEmpty(newUser.getPassword())) {
			String hashPassword = encrypt.hashPassword(newUser.getPassword());
			oldUser.setPassword(hashPassword);
		}
		oldUser.setUserId(userId);
		oldUser.getBrand().setBrandId(userId);
		User user = userDao.save(oldUser);
		UserResponse userResponse = Util.getUserResponse(user);
		CacheUtil.updateCacheObject(userRequestCache, token, userResponse);
		return userResponse;
	}
	
	public Brand getBrand(Long brandId) throws ResponseException {
		Brand brand = brandDao.findOne(brandId);
		if(null == brand){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.brand.not.found");
		}
		return brand;
	}

}
