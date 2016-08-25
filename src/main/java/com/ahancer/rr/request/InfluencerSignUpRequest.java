package com.ahancer.rr.request;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.ahancer.rr.models.InfluencerMedia;
import com.ahancer.rr.models.Resource;

public class InfluencerSignUpRequest {
	
	private String name;
	
	@NotNull(message="error.email.require")
	@Size(min=3,max=100,message="error.email.length")
	@Email(message="error.email.invalid")
	private String email;
	
	private String phoneNumber;
	
	private Resource profilePicture;
	
	@NotEmpty(message="error.influencer.signup.no.media")
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
