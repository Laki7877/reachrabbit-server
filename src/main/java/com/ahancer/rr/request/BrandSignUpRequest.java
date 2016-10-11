package com.ahancer.rr.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class BrandSignUpRequest implements Serializable {
	private static final long serialVersionUID = -167686710519896452L;
	
	@NotNull(message="error.email.require")
	@Size(min=3,max=100,message="error.email.length")
	@Email(message="error.email.invalid")
	private String email;
	
	@NotNull(message="error.name.require")
	@Size(min=1,max=100,message="error.name.length")
	private String name;
	
	@NotNull(message="error.phonenumber.require")
	@Size(min=1,max=20,message="error.phonenumber.length")
	private String phoneNumber;
	
	@NotNull(message="error.password.require")
	@Size(min=8,max=100,message="error.password.length")
	private String password;
	
	@NotNull(message="error.brand.name.require")
	@Size(min=1,max=100,message="error.brand.name.length")
	private String brandName;
	
	@Size(max=255,message="error.brand.companyName.length")
	private String companyName;
	
	@Size(max=1000,message="error.brand.companyAddress.length")
	private String companyAddress;
	
	@Size(max=255,message="error.brand.companyTaxId.length")
	private String companyTaxId;
	
	private Boolean isCompany;
	
	private String ref;
	
	public BrandSignUpRequest() {
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
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
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
}
