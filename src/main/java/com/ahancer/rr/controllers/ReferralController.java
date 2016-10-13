package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Referral;
import com.ahancer.rr.request.ReferralRequest;
import com.ahancer.rr.response.ReferralResponse;
import com.ahancer.rr.services.ReferralService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/referral")
public class ReferralController {
	
	@Autowired
	private ReferralService referralService;
	
	@ApiOperation(value = "Create new referral")
	@RequestMapping(method=RequestMethod.POST)
	@Authorization({Role.Admin})
	public Referral createReferral(ReferralRequest request) throws Exception {
		Referral referral = referralService.createReferral(request);
		return referral;
	}
	
	@ApiOperation(value = "Get referral pagenation")
	@RequestMapping(method=RequestMethod.POST)
	@Authorization({Role.Admin})
	public Page<ReferralResponse> getAllReferral(Pageable pageRequest) throws Exception {
		Page<ReferralResponse> page = referralService.findAll(pageRequest);
		return page;
	}

}
