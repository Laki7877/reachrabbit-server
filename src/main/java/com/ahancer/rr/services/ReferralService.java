package com.ahancer.rr.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ahancer.rr.models.Referral;
import com.ahancer.rr.request.ReferralRequest;
import com.ahancer.rr.response.ReferralResponse;

@Service
public interface ReferralService {
	public Referral createReferral(ReferralRequest request) throws Exception;
	public Page<ReferralResponse> findAll(String search, Pageable pageable) throws Exception;
}
