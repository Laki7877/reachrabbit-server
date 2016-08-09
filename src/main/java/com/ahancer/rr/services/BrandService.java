package com.ahancer.rr.services;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import com.ahancer.rr.models.Category;
import com.ahancer.rr.models.Media;
import com.ahancer.rr.models.User;
import com.ahancer.rr.utils.CacheUtil;
import com.ahancer.rr.utils.EncryptionUtil;
import com.ahancer.rr.utils.JwtUtil;
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

	@Autowired
	private JwtUtil jwt;
	
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
		brand.setBrandId(user.getUserId());
		user.setBrand(brandDao.save(brand));
		
		
		
		//Category
		Category category = new Category();
		category.setCategoryId(1L);
		
		//Create campaign
		Campaign campaign = new Campaign();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 5);
		campaign.setSubmissionDeadline(cal.getTime());
		campaign.setFromBudget(null);
		campaign.setToBudget(null);
		campaign.setBrandId(brand.getBrandId());
		campaign.setTitle("Campaign แรกของคุณ");
		campaign.setCategory(category);
		campaign.setDescription("นี่คือคำอธิบาย");
		campaign.setStatus(CampaignStatus.Draft);
		
		List<Media> allMedia = new ArrayList<Media>();
		mediaDao.findAll().forEach(allMedia::add);
		
		campaign.setMedia(allMedia);	
		campaignDao.save(campaign);
		
		return user;
	}
	
	public User updateBrandUser(Long userId, User newUser) throws ResponseException {
		User oldUser = userDao.findOne(userId);
		
		if(oldUser == null) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.brand.not.found");
		}
		Util.copyProperties(newUser, oldUser);
		
		if(newUser.getPassword() != null) {
			String hashPassword = encrypt.hashPassword(newUser.getPassword());
			oldUser.setPassword(hashPassword);
				
		}
		oldUser.setUserId(userId);
		oldUser.getBrand().setBrandId(userId);
		return userDao.save(oldUser);
	}
	public Brand getBrand(Long brandId) throws ResponseException {
		Brand brand = brandDao.findOne(brandId);
		if(null == brand){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.brand.not.found");
		}
		return brand;
	}

}
