package com.ahancer.rr.services;

import org.jinstagram.Instagram;
import org.springframework.stereotype.Service;

import com.ahancer.rr.models.Post;
import com.ahancer.rr.request.InstagramAuthenticationRequest;
import com.ahancer.rr.response.InstagramProfileResponse;
import com.ahancer.rr.response.OAuthenticationResponse;
import com.ahancer.rr.response.UserResponse;

@Service
public interface InstagramService {
	public String getAccessToken(String authorizationCode, String redirectUri) throws Exception;
	public String getAdminAccessToken() throws Exception;
	public void refreshAdminToken(String accessToken) throws Exception;
	public Boolean checkAdminToken() throws Exception;
	public Post getPostInfo(String postId) throws Exception;
	public InstagramProfileResponse getProfile(String socialId) throws Exception;
	public Instagram getInstance(String accessToken) throws Exception;
	public OAuthenticationResponse authenticate(String accessToken, String ip) throws Exception;
	public InstagramProfileResponse validateUser(UserResponse user,String token,InstagramAuthenticationRequest request) throws Exception;

}
