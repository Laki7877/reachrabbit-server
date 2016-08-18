package com.ahancer.rr.response;

import java.io.Serializable;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Bank;
import com.ahancer.rr.models.Resource;

public class UserResponse implements Serializable {
	
	private static final long serialVersionUID = 4211737273283905383L;
	private Long userId;
	private String name;
	private String email;
	private Resource profilePicture;
	private Bank bank;
	private String bankAccount;
	private String phoneNumber;
	private InfluencerResponse influencer;
	private BrandResponse brand;
	private Role role;
	
	public UserResponse(){
		
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

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
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
