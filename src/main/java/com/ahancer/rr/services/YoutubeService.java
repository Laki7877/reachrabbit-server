package com.ahancer.rr.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ahancer.rr.daos.MediaDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.response.OAuthenticationResponse;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;

@Service
public class YoutubeService {
	@Value("${youtube.appKey}")
	private String appKey;
	@Value("${youtube.appSecret}")
	private String appSecret;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private MediaDao mediaDao;
	
	private GoogleAuthorizationCodeFlow authorizationCodeFlow;
	
	@PostConstruct
	public void init() throws GeneralSecurityException, IOException {
		authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(),
				appKey,
				appSecret, 
				Collections.singleton(YouTubeScopes.YOUTUBE))
				.build();
	}
	
	public String getAccessToken(String authorizationCode, String redirectUri) throws IOException {
		return authorizationCodeFlow.newTokenRequest(authorizationCode).setRedirectUri(redirectUri).execute().getAccessToken();
	}
	public YouTube getInstance(String accessToken) throws GeneralSecurityException, IOException {
		Credential credential = new GoogleCredential().setAccessToken(accessToken);
		return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).build();
	}
	
	public OAuthenticationResponse authentication(String accessToken) throws Exception {
		YouTube youtube = getInstance(accessToken);
		YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
		channelRequest.setMine(true);
		channelRequest.setPart("snippet,statistics");
		
		ChannelListResponse channelResult = channelRequest.execute();
		
		List<Channel> channelsList = channelResult.getItems();
		
		if(channelsList == null) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.youtube.no.channel");
		}
		
		Channel channel = channelsList.get(0);
		List<OAuthenticationResponse.Page> pages = new ArrayList<OAuthenticationResponse.Page>();
		pages.add(new OAuthenticationResponse.Page(channel.getId(), channel.getStatistics().getSubscriberCount(), channel.getSnippet().getTitle()));
		
		AuthenticationResponse auth = authenticationService.influencerAuthentication(channel.getId(), "youtube");
		
		if(auth == null) {
			OAuthenticationResponse oauth = new OAuthenticationResponse();
			oauth.setName(channel.getSnippet().getTitle());
			
			oauth.setId(channel.getId());
			oauth.setMedia(mediaDao.findByMediaId("youtube"));
			oauth.setProfilePicture(channel.getSnippet().getThumbnails().getHigh().getUrl());
			oauth.setPages(pages);
			return oauth;
		} else {
			return new OAuthenticationResponse(auth.getToken());
		}
	}
}
	
