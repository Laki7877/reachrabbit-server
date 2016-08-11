package com.ahancer.rr.services;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ahancer.rr.daos.MediaDao;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.response.OAuthenticationResponse;

@Service
public class InstagramService {
	@Value("${instagram.appKey}")
	private String appKey;
	@Value("${instagram.appSecret}")
	private String appSecret;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private MediaDao mediaDao;
	
	public String getAccessToken(String authorizationCode, String redirectUri) {
		org.jinstagram.auth.oauth.InstagramService service = new InstagramAuthService()
				.apiKey(appKey)
				.apiSecret(appSecret)
				.callback(redirectUri)
				.build();
		return service.getAccessToken(new Verifier(authorizationCode)).getToken();
	}
	public Instagram getInstance(String accessToken) {
		return new Instagram(new Token(accessToken, appSecret));
	}
	public OAuthenticationResponse authenticate(String accessToken) throws InstagramException {
		Instagram instagram = getInstance(accessToken);
		UserInfoData userInfo = instagram.getCurrentUserInfo().getData();
		
		AuthenticationResponse auth = authenticationService.influencerAuthentication(userInfo.getId(), "instagram");
		
		if(auth == null) {
			OAuthenticationResponse oauth = new OAuthenticationResponse();
			oauth.setName(userInfo.getFullName());
			oauth.setId(userInfo.getId());
			oauth.setMedia(mediaDao.findByMediaId("instagram"));
			oauth.setProfilePicture(userInfo.getProfilePicture());
			oauth.setPages(null);
			return oauth;
		} else {
			return (OAuthenticationResponse)auth;
		}
	}
}
