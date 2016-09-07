package com.ahancer.rr.response;

import java.io.Serializable;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.InfluencerMedia;
import com.ahancer.rr.models.Resource;

public class UserResponse implements Serializable {
	
	private static final long serialVersionUID = 4211737273283905383L;
	private Long userId;
	private String name;
	private String email;
	private Resource profilePicture;
	private String phoneNumber;
	private InfluencerResponse influencer;
	private BrandResponse brand;
	private Role role;
	
	public UserResponse(){
		
	}
	
	public String getPageId(String mediaId) throws Exception {
		InfluencerMedia media = null;
		
		if(getInfluencer() == null) {
			throw new Exception();
		}
		
		for(InfluencerMedia element : getInfluencer().getInfluencerMedias()) {
			System.out.println(mediaId);
			System.out.println(element.getMedia().getMediaId());
			System.out.println(element.getMedia().getMediaId().length());
			if(element.getMedia().getMediaId().equals(mediaId)) {
				media = element;
			}
		}
		
		return media.getPageId();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public InfluencerResponse getInfluencer() {
		return influencer;
	}

	public void setInfluencer(InfluencerResponse influencer) {
		this.influencer = influencer;
	}

	public BrandResponse getBrand() {
		return brand;
	}

	public void setBrand(BrandResponse brand) {
		this.brand = brand;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
}
