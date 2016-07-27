package com.ahancer.rr.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class AuthenticationRequest implements Serializable {

	private static final long serialVersionUID = -1109021554580568494L;
	
	@NotNull(message="error.email.require")
	@Size(min=3,max=255,message="error.email.length")
	@Email(message="error.email.invalid")
	private String email;
	
	@NotNull(message="error.password.require")
	private String password;

	public AuthenticationRequest() {
		super();
	}

	public AuthenticationRequest(String email, String password) {
		this.setEmail(email);
		this.setPassword(password);
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
	
	
	
}
