package com.ahancer.rr.response;

import java.io.Serializable;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Brand;

public class BrandResponse implements Serializable {

	private static final long serialVersionUID = 4549371860913222596L;
	private Long brandId;
	private String brandName;
	private String about;
	private String website;
	private UserResponse user;
	private String companyName;
	private String companyAddress;
	private String companyTaxId;
	private Boolean isCompany;
	public BrandResponse(){
		
	}
	public BrandResponse(Brand brand,String roleValue){
		Role role = Role.valueOf(roleValue);
		user = new UserResponse();
		user.setName(brand.getUser().getName());
		user.setProfilePicture(brand.getUser().getProfilePicture());
		user.setUserId(brand.getUser().getUserId());
		switch (role) {
			case Admin:
				user.setEmail(brand.getUser().getEmail());
				user.setPhoneNumber(brand.getUser().getPhoneNumber());
				break;
			case Brand:
				user.setEmail(brand.getUser().getEmail());
				user.setPhoneNumber(brand.getUser().getPhoneNumber());
				this.companyName = brand.getCompanyName();
				this.companyAddress = brand.getCompanyAddress();
				this.companyTaxId = brand.getCompanyTaxId();
				this.isCompany = brand.getIsCompany();
				break;
			case Influencer:
				break;
			default:
				break;
		}
		this.brandId = brand.getBrandId();
		this.brandName = brand.getBrandName();
		this.about = brand.getAbout();
		this.website = brand.getWebsite();
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	
	public UserResponse getUser() {
		return user;
	}

	public void setUser(UserResponse user) {
		this.user = user;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getCompanyTaxId() {
		return companyTaxId;
	}
	public void setCompanyTaxId(String companyTaxId) {
		this.companyTaxId = companyTaxId;
	}
	public Boolean getIsCompany() {
		return isCompany;
	}
	public void setIsCompany(Boolean isCompany) {
		this.isCompany = isCompany;
	}
}
