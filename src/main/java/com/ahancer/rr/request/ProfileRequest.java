package com.ahancer.rr.request;

import java.io.Serializable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.ahancer.rr.models.Resource;
import com.ahancer.rr.response.BrandResponse;
import com.ahancer.rr.response.InfluencerResponse;

public class ProfileRequest implements Serializable {
	
	private static final long serialVersionUID = -5114386336899455015L;
	@Size(min=1,max=100,message="error.name.length")
	private String name;
	@Size(min=3,max=100,message="error.email.length")
	@Email(message="error.email.invalid")
	private String email;
	@Size(min=8,max=100,message="error.password.length")
	private String password;
	private Resource profilePicture;
	@Pattern(regexp="[0-9]*", message="error.phone")
	private String phoneNumber;
	private BrandResponse brand;
	private InfluencerResponse influencer;
	
	public ProfileRequest(){
		
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Resource getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(Resource profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public BrandResponse getBrand() {
		return brand;
	}

	public void setBrand(BrandResponse brand) {
		this.brand = brand;
	}

	public InfluencerResponse getInfluencer() {
		return influencer;
	}

	public void setInfluencer(InfluencerResponse influencer) {
		this.influencer = influencer;
	}

}
