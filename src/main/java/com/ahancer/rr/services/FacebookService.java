package com.ahancer.rr.services;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Service;

import com.ahancer.rr.models.Post;
import com.ahancer.rr.response.FacebookProfileResponse;
import com.ahancer.rr.response.OAuthenticationResponse;

@Service
public interface FacebookService {
	public Facebook getInstance(String accessToken) throws Exception;
	public String getAccessToken(String authorizationCode, String redirectUri) throws Exception;
	public String getAppAccessToken() throws Exception;
	public Post getPostInfo(String postId) throws Exception;
	public FacebookProfileResponse getProfile(String pageId) throws Exception;
	public OAuthenticationResponse authenticate(String accessToken, String ip) throws Exception;

}
