package com.ahancer.rr.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.ReferralDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Referral;
import com.ahancer.rr.models.User;
import com.ahancer.rr.request.ReferralRequest;
import com.ahancer.rr.response.ReferralResponse;
import com.ahancer.rr.utils.EncryptionUtil;

@Service
public class ReferralService {
	@Autowired
	private ReferralDao referralDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private EncryptionUtil encryptionUtil;
	@Autowired
	private ProposalDao proposalDao;
	
	public Referral createReferral(ReferralRequest request) throws Exception {
		User user = userDao.findByEmail(request.getEmail());
		if(null == user) {
			user = new User();
			user.setEmail(request.getEmail());
			user.setRole(Role.Partner);
			encryptionUtil.hashPassword("P@ssw0rd");
			user = userDao.save(user);
		}
		if(!Role.Partner.equals(user.getRole())){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.referral.user.invalid");
		}
		Referral referral = new Referral();
		referral.setReferralId(UUID.randomUUID().toString().replace("-", ""));
		referral.setPartnerId(user.getUserId());
		referral.setCommission(request.getCommission());
		referral.setDescription(request.getDescription());
		referral = referralDao.save(referral);
		referral.setPartner(user);
		return referral;
	}
	
	public Page<ReferralResponse> findAll(String search, Pageable pageable) {
		Page<ReferralResponse> page = null;
		referralDao.findAll();
		if(StringUtils.isEmpty(search)) {
			page = referralDao.findAll(pageable);
		} else {
			page = referralDao.findAllBySearch(search, pageable);
		}
		List<ProposalStatus> statuses = new ArrayList<ProposalStatus>(2);
		statuses.add(ProposalStatus.Working);
		statuses.add(ProposalStatus.Complete);
		for(ReferralResponse referral : page.getContent()){
			referral.setSignUpCount(userDao.countByReferralId(referral.getReferralId()));
			referral.setPaidWorkRoomCount(proposalDao.countByCampaignBrandUserReferralIdAndStatusIn(referral.getReferralId(),statuses));
		}
		return page;
	}

}
