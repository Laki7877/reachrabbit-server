package com.ahancer.rr.services;



import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.BrandDao;
import com.ahancer.rr.daos.ReferralDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.models.User;
import com.ahancer.rr.request.BrandSignUpRequest;
import com.ahancer.rr.request.ProfileRequest;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.utils.CacheUtil;
import com.ahancer.rr.utils.EncryptionUtil;
import com.ahancer.rr.utils.Util;

@Service
@Transactional(rollbackFor=Exception.class)
public class BrandService {
	@Autowired
	private CacheUtil cacheUtil;
	@Autowired
	private BrandDao brandDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private EncryptionUtil encrypt;
	@Autowired
	private ReferralDao referralDao;
	@Value("${ui.host}")
	private String uiHost;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private EmailService emailService;

	public User signUpBrand(BrandSignUpRequest request,Locale locale) throws Exception {
		//Validate duplicate Email
		int emailCount = userDao.countByEmail(request.getEmail());
		if(0 < emailCount) {
			throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"error.email.duplicate");
		}
		//Setup user object
		User user = new User();
		user.setEmail(request.getEmail());
		user.setName(request.getName());
		user.setPhoneNumber(request.getPhoneNumber());
		String hashPassword = encrypt.hashPassword(request.getPassword());
		user.setPassword(hashPassword);
		user.setRole(Role.Brand);
		if(StringUtils.isNotEmpty(request.getRef())) {
			Long count = referralDao.countByReferralId(request.getRef());
			if(count > 0L){
				user.setReferralId(request.getRef());
			}
		}
		user = userDao.save(user);
		//Setup brand object
		Brand brand = new Brand();
		brand.setBrandName(request.getBrandName());
		brand.setCompanyAddress(request.getCompanyAddress());
		brand.setCompanyName(request.getCompanyName());
		brand.setCompanyTaxId(request.getCompanyTaxId());
		brand.setIsCompany(null == request.getIsCompany() ? false : request.getIsCompany());
		brand.setBrandId(user.getUserId());
		brand = brandDao.save(brand);
		user.setBrand(brand);
		String to = user.getEmail();
		String subject = messageSource.getMessage("email.brand.signup.subject",null,locale);
		String body = messageSource.getMessage("email.brand.signup.message",null,locale)
				.replace("{{Brand Name}}", brand.getBrandName())
				.replace("{{Registered Email}}", user.getEmail())
				.replace("{{Host}}", uiHost);
		emailService.send(to,subject, body);
		return user;
	}

	public UserResponse updateBrandUser(Long userId, ProfileRequest request,String token) throws Exception {
		User user = userDao.findOne(userId);
		if(null == user || !Role.Brand.equals(user.getRole())) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.brand.not.found");
		}
		//Validate duplicate Email
		if(StringUtils.isNotEmpty(user.getEmail()) 
				&& !user.getEmail().equals(request.getEmail())) {
			int countEmail = userDao.countByEmail(request.getEmail());
			if(0 < countEmail){
				throw new ResponseException(HttpStatus.BAD_REQUEST,"error.email.duplicate");
			}
		}
		user.setEmail(request.getEmail());
		user.setName(request.getName());
		user.setPhoneNumber(request.getPhoneNumber());
		Brand brand = user.getBrand();
		brand.setBrandName(request.getBrand().getBrandName());
		brand.setAbout(request.getBrand().getAbout());
		brand.setWebsite(request.getBrand().getWebsite());
		brand.setCompanyAddress(request.getBrand().getCompanyAddress());
		brand.setCompanyName(request.getBrand().getCompanyName());
		brand.setCompanyTaxId(request.getBrand().getCompanyTaxId());
		brand.setIsCompany(request.getBrand().getIsCompany());
		brand.setBrandId(user.getUserId());
		user.setBrand(brand);
		user.setProfilePicture(request.getProfilePicture());
		if(StringUtils.isNotEmpty(request.getPassword())) {
			String hashPassword = encrypt.hashPassword(request.getPassword());
			user.setPassword(hashPassword);
		}
		user = userDao.save(user);
		UserResponse userResponse = Util.getUserResponse(user);
		cacheUtil.updateCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
		return userResponse;
	}

}
