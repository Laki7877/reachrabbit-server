package com.ahancer.rr.response;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

	private static final long serialVersionUID = 3588938921062154848L;
	private String token;

	public AuthenticationResponse() {
		super();
	}

	public AuthenticationResponse(String token) {
		this.setToken(token);
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
