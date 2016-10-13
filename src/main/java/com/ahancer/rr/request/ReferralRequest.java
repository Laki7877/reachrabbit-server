package com.ahancer.rr.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class ReferralRequest implements Serializable {
	
	private static final long serialVersionUID = 7586128389134091747L;
	
	@NotNull(message="error.email.require")
	@Size(min=3,max=100,message="error.email.length")
	@Email(message="error.email.invalid")
	private String email;
	
	private String description;
	
	@NotNull(message="error.referral.commission")
	private Double commission;

	public ReferralRequest() {
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

}
