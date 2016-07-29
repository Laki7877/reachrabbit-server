package com.ahancer.rr.services;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.exception.ResponseException;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.User;
import facebook4j.auth.AccessToken;

@Service
@Transactional(rollbackFor=Exception.class)
public class FacebookService {
	private Facebook getFacebookInstance(String accessToken) {
		return new FacebookFactory().getInstance(new AccessToken(accessToken));
	}
	/**
	 * Get facebook short-lived token with authorization code		
	 * @param authorizationCode AUthorization Code
	 * @return Facebook token as string
	 * @throws ResponseException
	 */
	public String getAccessToken(String authorizationCode) throws ResponseException {
		Facebook facebook = new FacebookFactory().getInstance();
		try {
			return facebook.getOAuthAccessToken(authorizationCode).getToken();
		} catch(Exception e) {
			throw new ResponseException("error.oauth.facebook.invalid", HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Get facebook user	
	 * @param accessToken
	 * @return Facebook4j user
	 * @throws ResponseException
	 */
	public User getProfile(String accessToken) throws ResponseException {
		Facebook facebook = getFacebookInstance(accessToken);
		try {
			return facebook.getMe();			
		} catch(Exception e) {
			throw new ResponseException("error.oauth.facebook.invalid", HttpStatus.BAD_REQUEST);
		}
	}
}
