package com.ahancer.rr.response;

import java.io.Serializable;

public class InstagramAuthenticationResponse implements Serializable {
	private static final long serialVersionUID = 6449772549325048964L;
	private String status;
	private Boolean authenticated;
	private String user;
	private Boolean reactivated;

	public InstagramAuthenticationResponse() {
		
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public Boolean getReactivated() {
		return reactivated;
	}

	public void setReactivated(Boolean reactivated) {
		this.reactivated = reactivated;
	}

	
}
