package com.ahancer.rr.services;

import org.springframework.stereotype.Service;

import com.ahancer.rr.models.User;
import com.ahancer.rr.response.AuthenticationResponse;

@Service
public interface AuthenticationService {
	public AuthenticationResponse brandAuthentication(String email, String password, String ip) throws Exception;
	public AuthenticationResponse adminAuthentication(String email, String password, String ip) throws Exception;
	public AuthenticationResponse influencerAuthentication(String socialId, String providerName, String ip) throws Exception;
	public AuthenticationResponse generateTokenFromUser(User user, String ip) throws Exception;
	public User getUserById(Long userId) throws Exception;
	public AuthenticationResponse influencerEmailAuthentication(String email, String password, String ip) throws Exception;
}
