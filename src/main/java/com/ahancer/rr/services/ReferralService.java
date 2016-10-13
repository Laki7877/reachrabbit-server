package com.ahancer.rr.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.ReferralDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Referral;
import com.ahancer.rr.models.User;
import com.ahancer.rr.request.ReferralRequest;
import com.ahancer.rr.utils.EncryptionUtil;

@Service
public class ReferralService {
	
	@Autowired
	private ReferralDao referralDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private EncryptionUtil encryptionUtil;
	
	public Referral createReferral(ReferralRequest request) throws Exception {
		User user = userDao.findByEmail(request.getEmail());
		if(null == user) {
			user = new User();
			user.setEmail(request.getEmail());
			user.setName(request.getEmail().split("@")[0]);
			user.setRole(Role.Partner);
			encryptionUtil.hashPassword("P@ssw0rd");
			user = userDao.save(user);
		}
		if(!Role.Partner.equals(user.getRole())){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.referral.user.invalid");
		}
		Referral referral = new Referral();
		referral.setReferralId(UUID.randomUUID().toString().replace("-", ""));
		referral.setUserId(user.getUserId());
		referral.setDescription(request.getDescription());
		referral = referralDao.save(referral);
		referral.setUser(user);
		return referral;
	}
	

}
