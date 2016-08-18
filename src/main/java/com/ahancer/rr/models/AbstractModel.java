package com.ahancer.rr.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public abstract class AbstractModel {

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdAt",updatable=false)
	private Date createdAt;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedAt")
	private Date updatedAt;
	
	
	@JsonIgnore
	@Column(name="createdBy")
	private Long createdBy;
	
	@JsonIgnore
	@Column(name="updatedBy")
	private Long updatedBy;
	
	@PrePersist
	protected void onCreate() {
		updatedAt = createdAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = new Date();
	}
	
	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	

}
