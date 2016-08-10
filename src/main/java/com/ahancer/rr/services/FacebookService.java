package com.ahancer.rr.services;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.social.facebook.api.Account;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.response.OAuthenticationResponse;

@Service
@Transactional(rollbackFor=Exception.class)
public class FacebookService {
	@Value("${facebook.appKey}")
	private String appKey;
	@Value("${faceobok.appSecret")
	private String appSecret;
	private FacebookConnectionFactory connectionFactory;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostConstruct
	public void init() {
		System.out.println("LAUNCHED");
		connectionFactory = new FacebookConnectionFactory(appKey, appSecret);
	}
	public Facebook getInstance(String accessToken) throws ResponseException {
		Facebook fb = new FacebookTemplate(accessToken);
		if(!fb.isAuthorized()) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.unauthorized");
		}
		return fb;
	}
	public String getAccessToken(String authorizationCode, String redirectUri) {
		AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(authorizationCode, redirectUri, null);
		return accessGrant.getAccessToken();
	}
	
	public OAuthenticationResponse authenticate(String accessToken) throws ResponseException {
		Facebook fb = getInstance(accessToken);
		org.springframework.social.facebook.api.User fbUser = fb.userOperations().getUserProfile();
		List<Account> accounts = fb.pageOperations().getAccounts();
		List<OAuthenticationResponse.Page> pages = new ArrayList<OAuthenticationResponse.Page>(); {
		}; 
		
		for(Account account : accounts) {
			pages.add(new OAuthenticationResponse.Page(account.getId(), fb.pageOperations().getPage(account.getId()).getEngagement().getCount()));
		}
		
		AuthenticationResponse auth = authenticationService.influencerAuthentication(fbUser.getId(), "facebook");

		if(auth == null) {
			OAuthenticationResponse oauth = new OAuthenticationResponse();
			oauth.setId(fbUser.getId());
			oauth.setName(fbUser.getName());
			oauth.setEmail(fbUser.getEmail());
			oauth.setPages(pages);
			
			return oauth;
		} else {
			return (OAuthenticationResponse)auth;
		}
	}
}
