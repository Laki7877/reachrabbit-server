package com.ahancer.rr.services;

import org.springframework.stereotype.Service;

import com.ahancer.rr.models.Post;
import com.ahancer.rr.response.OAuthenticationResponse;
import com.ahancer.rr.response.YouTubeProfileResponse;
import com.google.api.services.youtube.YouTube;

@Service
public interface YoutubeService {
	public String getAccessToken(String authorizationCode, String redirectUri) throws Exception;
	public YouTube getInstance(String accessToken) throws Exception;
	public Post getPostInfo(String postId) throws Exception;
	public YouTubeProfileResponse getVideoFeed(String channelId) throws Exception;
	public OAuthenticationResponse authentication(String accessToken, String ip) throws Exception;
}
