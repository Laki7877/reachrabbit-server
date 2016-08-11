package com.ahancer.rr.request;

import java.io.Serializable;

public class OAuthenticationRequest implements Serializable{

	private static final long serialVersionUID = 608803236524033890L;
	private String code; //Authorization code
	private String redirectUri;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}	
}
