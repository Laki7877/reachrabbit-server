package com.ahancer.rr.services;


import java.util.List;

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
	
	@Autowired
	public FacebookService() {
		connectionFactory = new FacebookConnectionFactory(appKey, appSecret);
	}
	public Facebook getInstance(String accessToken) throws ResponseException {
		Facebook fb = new FacebookTemplate(accessToken);
		if(!fb.isAuthorized()) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.unauthorized");
		}
		return fb;
	}
	public String getAccessToken(String authorizationCode) {
		AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeCredentialsForAccess(authorizationCode, null, null);
		return accessGrant.getAccessToken();
	}
	
	public OAuthenticationResponse authenticate(String accessToken, boolean createNew) throws ResponseException {
		Facebook fb = getInstance(accessToken);
		org.springframework.social.facebook.api.User fbUser = fb.userOperations().getUserProfile();
		List<Account> accounts = fb.pageOperations().getAccounts();
		
		AuthenticationResponse auth = authenticationService.influencerAuthentication(fbUser.getId(), "facebook");

		if(auth == null) {
			OAuthenticationResponse oauth = new OAuthenticationResponse();
			oauth.setName(fbUser.getName());
			oauth.setEmail(fbUser.getEmail());
			oauth.setAccounts(accounts);
			return oauth;
		} else {
			return (OAuthenticationResponse)auth;
		}
	}
}
