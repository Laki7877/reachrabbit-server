package com.ahancer.rr.services;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.ahancer.rr.models.User;
import com.ahancer.rr.request.InfluencerCommissionRequest;
import com.ahancer.rr.request.InfluencerSignUpRequest;
import com.ahancer.rr.request.PayoutRequest;
import com.ahancer.rr.request.ProfileRequest;
import com.ahancer.rr.response.UserResponse;

@Service
public interface InfluencerService {
	public UserResponse updateInfluencerUser(Long userId, ProfileRequest request, String token) throws Exception;
	public UserResponse updateBankDetail(PayoutRequest request,Long influencerId, String token) throws Exception;
	public User signupInfluencer(InfluencerSignUpRequest request,Locale locale) throws Exception;
	public UserResponse updateCommission(InfluencerCommissionRequest commission, Long influencerId) throws Exception;
}
