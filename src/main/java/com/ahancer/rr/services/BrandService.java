package com.ahancer.rr.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.BrandDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.models.User;
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
	
	public String signUpBrand(Brand brand) throws ResponseException {
		User user = brand.getUser();
		String hashPassword = encrypt.hashPassword(user.getPassword());
		user.setPassword(hashPassword);
		
		//Set role
		brand.getUser().setRole(Role.Brand);
		
		userDao.save(brand.getUser());
		brandDao.save(brand);
		
		String token = jwt.generateToken(user.getUserId());
		return token;
	}

}
