package com.ahancer.rr.response;

import java.io.Serializable;

import com.ahancer.rr.models.Resource;

public class OAuthenticationResponse extends AuthenticationResponse implements Serializable {
	
	private static final long serialVersionUID = -8190324180657188609L;
	private String provider;
	private String name;
	private String id;
	private Integer followerCount;
	private Resource picture;

	public OAuthenticationResponse() {
		super(null);
		// TODO Auto-generated constructor stub
	}
	public OAuthenticationResponse(String token) {
		super(token);
		// TODO Auto-generated constructor stub
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getFollowerCount() {
		return followerCount;
	}
	public void setFollowerCount(Integer followerCount) {
		this.followerCount = followerCount;
	}
	public Resource getPicture() {
		return picture;
	}
	public void setPicture(Resource picture) {
		this.picture = picture;
	}	
}
