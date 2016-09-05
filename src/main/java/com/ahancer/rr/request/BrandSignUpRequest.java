package com.ahancer.rr.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
	@Pattern(regexp="(^$|[0-9]{10})",message="error.phonenumber.number.only")
	private String phoneNumber;
	
	@NotNull(message="error.password.require")
	@Size(min=8,max=100,message="error.password.length")
	private String password;
	
	@NotNull(message="error.brand.name.require")
	@Size(min=1,max=100,message="error.brand.name.length")
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
