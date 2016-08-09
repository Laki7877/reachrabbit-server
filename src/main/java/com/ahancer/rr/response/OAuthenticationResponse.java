package com.ahancer.rr.response;

import java.io.Serializable;
import java.util.List;

import org.springframework.social.facebook.api.Account;

import com.ahancer.rr.models.Media;
import com.ahancer.rr.models.Resource;

public class OAuthenticationResponse extends AuthenticationResponse implements Serializable {
	
	private static final long serialVersionUID = -8190324180657188609L;
	private Media media;
	private String name;
	private String email;
	private String id;
	private Integer followerCount;
	private List<Account> accounts;
	private Resource profilePicture;
	
	
	public OAuthenticationResponse() {
		super(null);
		// TODO Auto-generated constructor stub
	}
	public OAuthenticationResponse(String token) {
		super(token);
		// TODO Auto-generated constructor stub
	}
	public Media getMedia() {
		return media;
	}
	public void setMedia(Media media) {
		this.media = media;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	public Resource getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(Resource profilePicture) {
		this.profilePicture = profilePicture;
	}
	
}
