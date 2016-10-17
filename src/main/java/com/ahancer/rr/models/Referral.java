package com.ahancer.rr.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity(name="referral")
public class Referral extends AbstractModel implements Serializable {
	
	private static final long serialVersionUID = 2753227724593896811L;

	@Id
	@Column(name="referralId",length=32)
	private String referralId;
	
	@Column(name="partnerId", nullable = false)
	private Long partnerId;
	
	@MapsId("partnerId")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="partnerId")
	private User partner;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "referral")
	private Set<User> users = new HashSet<User>(0);
	
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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}
