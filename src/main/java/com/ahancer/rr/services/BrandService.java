package com.ahancer.rr.services;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.ahancer.rr.models.User;
import com.ahancer.rr.request.BrandSignUpRequest;
import com.ahancer.rr.request.ProfileRequest;
import com.ahancer.rr.response.UserResponse;

@Service
public interface BrandService {
	public User signUpBrand(BrandSignUpRequest request,Locale locale) throws Exception;
	public UserResponse updateBrandUser(Long userId, ProfileRequest request,String token) throws Exception;
}
