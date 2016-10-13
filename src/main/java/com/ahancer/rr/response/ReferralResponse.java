package com.ahancer.rr.response;

import java.io.Serializable;
import java.util.Date;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Referral;

public class ReferralResponse implements Serializable  {

	private static final long serialVersionUID = 237223369005154722L;
	
	private String referralId;
	private String email;
	private String description;
	private Long signUpCount;
	private Long paidWorkRoomCount;
	private Date createdAt;
	
	public ReferralResponse(Referral referral,String roleValue) {
		Role role = Role.valueOf(roleValue);
		switch (role) {
			case Admin:
				break;
			default:
				break;
		}
		this.email = referral.getUser().getEmail();
		this.description = referral.getDescription();
		this.referralId = referral.getReferralId();
		this.createdAt = referral.getCreatedAt();
	}
	public String getReferralId() {
		return referralId;
	}
	public void setReferralId(String referralId) {
		this.referralId = referralId;
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
	public Long getSignUpCount() {
		return signUpCount;
	}
	public void setSignUpCount(Long signUpCount) {
		this.signUpCount = signUpCount;
	}
	public Long getPaidWorkRoomCount() {
		return paidWorkRoomCount;
	}
	public void setPaidWorkRoomCount(Long paidWorkRoomCount) {
		this.paidWorkRoomCount = paidWorkRoomCount;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
