package com.ahancer.rr.request;

import java.util.HashSet;
import java.util.Set;

import com.ahancer.rr.models.InfluencerMedia;
import com.ahancer.rr.models.Resource;

public class InfluencerSignUpRequest {
	private String name;
	private String email;
	private String phoneNumber;
	private Resource profilePicture;
	private Set<InfluencerMedia> influencerMedia = new HashSet<InfluencerMedia>(0);
	public InfluencerSignUpRequest() {
	}
	public InfluencerSignUpRequest(String name, String email, String phoneNumber, Resource profilePicture,
			Set<InfluencerMedia> influencerMedia) {
		super();
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.profilePicture = profilePicture;
		this.influencerMedia = influencerMedia;
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Resource getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(Resource profilePicture) {
		this.profilePicture = profilePicture;
	}
	public Set<InfluencerMedia> getInfluencerMedia() {
		return influencerMedia;
	}
	public void setInfluencerMedia(Set<InfluencerMedia> influencerMedia) {
		this.influencerMedia = influencerMedia;
	}
	
}
