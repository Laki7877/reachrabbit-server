package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity(name="referral")
public class Referral extends AbstractModel implements Serializable {
	
	private static final long serialVersionUID = 2753227724593896811L;

	@Id
	@Column(name="referralId",length=32)
	private String referralId;
	
	@Column(name="userId", nullable = false)
	private Long userId;
	
	@MapsId("userId")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")
	private User user;
	
	@Column(name="description",length=255)
	private String description;
	
	@Column(name="commission",scale=10,precision=3)
	private Double commission;

	public String getReferralId() {
		return referralId;
	}

	public void setReferralId(String referralId) {
		this.referralId = referralId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
