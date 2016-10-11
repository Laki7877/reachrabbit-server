package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="referral")
public class Referral extends AbstractModel implements Serializable {
	
	private static final long serialVersionUID = 2753227724593896811L;

	@Id
	@Column(name="referralId",length=32)
	private String referralId;
	
	@Column(name="groupName",length=255)
	private String groupName;
	
	
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
