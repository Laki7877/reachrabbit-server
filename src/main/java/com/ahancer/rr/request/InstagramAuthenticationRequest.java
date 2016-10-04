package com.ahancer.rr.request;

import java.io.Serializable;

public class InstagramAuthenticationRequest implements Serializable {
	private static final long serialVersionUID = -1060869588047873831L;
	private String username;
	private String password;
	
	public InstagramAuthenticationRequest() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
