package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name="referral")
public class Referral extends AbstractModel implements Serializable {
	
	private static final long serialVersionUID = 2753227724593896811L;

	@Id
	@Column(name="referralId",length=32)
	private String referralId;
	
	@Column(name="partnerId", nullable = false)
	private Long partnerId;
	
	@MapsId("partnerId")
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="partnerId")
	@JsonManagedReference(value="user-partner")
	private User partner;
	
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

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public User getPartner() {
		return partner;
	}

	public void setPartner(User partner) {
		this.partner = partner;
	}
}
