package com.ahancer.rr.request;

public class BrandSignUpRequest {
	private String email;
	private String name;
	private String phoneNumber;
	private String password;
	private String brandName;
	public BrandSignUpRequest(String email, String name, String brandName, String phoneNumber, String password) {
		super();
		this.email = email;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.setBrandName(brandName);
	}
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

}
