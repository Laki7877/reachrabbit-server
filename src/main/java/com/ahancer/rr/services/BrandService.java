package com.ahancer.rr.services;



import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.BrandDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.models.User;
import com.ahancer.rr.utils.CacheUtil;
import com.ahancer.rr.utils.EncryptionUtil;
import com.ahancer.rr.utils.JwtUtil;

@Service
@Transactional(rollbackFor=Exception.class)
public class BrandService {


	@Autowired
	private BrandDao brandDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private EncryptionUtil encrypt;

	@Autowired
	private JwtUtil jwt;
	
	@Value("${reachrabbit.cache.userrequest}")
	private String userRequestCache;

	public String signUpBrand(User user) throws ResponseException {
		
		Brand brand = user.getBrand();
		if(null == brand){
			throw new ResponseException();
		}
		
		//Validate duplicate Email
		int emailCount = userDao.countByEmail(user.getEmail());
		if(emailCount > 0) {
			throw new ResponseException("error.email.duplicate",HttpStatus.BAD_REQUEST);
		}
		String hashPassword = encrypt.hashPassword(user.getPassword());
		user.setPassword(hashPassword);
		//Set role
		user.setRole(Role.Brand);
		user.setBrand(null);
		userDao.save(user);
		brand.setBrandId(user.getUserId());
		brandDao.save(brand);
		String token = jwt.generateToken(user.getUserId());
		CacheUtil.putCacheObject(userRequestCache, token, user);
		return token;
	}
	
	public User updateBrandUser(Long userId, User newUser) {
		User oldUser = userDao.findOne(userId);
		BeanUtils.copyProperties(newUser, oldUser);
		oldUser.setUserId(userId);
		oldUser.getBrand().setBrandId(userId);
		return userDao.save(oldUser);
	}
	public Brand getBrand(Long brandId) throws ResponseException {
		Brand brand = brandDao.findOne(brandId);
		if(null == brand){
			throw new ResponseException("error.brand.not.found",HttpStatus.BAD_REQUEST);
		}
		return brand;
	}

}
