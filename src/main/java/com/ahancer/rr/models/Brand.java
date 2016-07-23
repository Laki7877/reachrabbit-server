package com.ahancer.rr.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="brand")
public class Brand {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long bankId;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId",nullable=false)
	private User user;
	
	@Column(name="brandName",length=255)
	private String brandName;
	
	@Column(name="createdBy")
	public Long createdBy;
	
	@Column(name="updatedBy")
	public Long updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date deletedAt;
	
	

	public Brand() {
		
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	
	
}
