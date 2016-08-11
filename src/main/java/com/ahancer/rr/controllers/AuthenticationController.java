package com.ahancer.rr.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.request.AuthenticationRequest;
import com.ahancer.rr.request.OAuthenticationRequest;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.response.OAuthenticationResponse;
import com.ahancer.rr.services.AuthenticationService;
import com.ahancer.rr.services.FacebookService;
import com.ahancer.rr.services.YoutubeService;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@Autowired
	private FacebookService facebookService;
	@Autowired
	private YoutubeService youtubeService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@RequestMapping(value = "/login" ,method = RequestMethod.POST)
	@ResponseBody
	public AuthenticationResponse brandAuthenticationRequest(@Valid @RequestBody AuthenticationRequest authenticationRequest) 
			throws Exception {
		AuthenticationResponse authen = authenticationService.brandAuthentication(authenticationRequest.getEmail(), authenticationRequest.getPassword());
		if(null == authen) {
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.unauthorize");
		}else {
			return authen;
		}
	}
	
	@RequestMapping(value = "/facebook" ,method = RequestMethod.POST)
	public OAuthenticationResponse facebookAuthenticationRequest(@Valid @RequestBody OAuthenticationRequest oauthenticationRequest) throws ResponseException {
		return facebookService.authenticate(facebookService.getAccessToken(oauthenticationRequest.getCode(), oauthenticationRequest.getRedirectUri()));
	}	

	
	@RequestMapping(value = "/instagram" ,method = RequestMethod.POST)
	public OAuthenticationResponse instagramAuthenticationRequest(@Valid @RequestBody OAuthenticationRequest oauthenticationRequest) throws ResponseException {
		return facebookService.authenticate(facebookService.getAccessToken(oauthenticationRequest.getCode(), oauthenticationRequest.getRedirectUri()));
	}	
	
	@RequestMapping(value = "/youtube" ,method = RequestMethod.POST)
	public OAuthenticationResponse youtubeAuthenticationRequest(@Valid @RequestBody OAuthenticationRequest oauthenticationRequest) throws Exception {
		return youtubeService.authentication(youtubeService.getAccessToken(oauthenticationRequest.getCode(), oauthenticationRequest.getRedirectUri()));
	}
	
}
