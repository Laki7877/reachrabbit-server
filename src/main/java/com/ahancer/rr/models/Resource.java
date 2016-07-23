package com.ahancer.rr.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ahancer.rr.custum.type.ResourceType;

@Entity(name="resource")
public class Resource {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long resourceId;

	@Column(name="resourcePath",length=255)
	private String resourcePath;

	@Enumerated(EnumType.STRING)
	private ResourceType resourceType;

	@Temporal(TemporalType.TIMESTAMP)
	public Date createdAt;

	@Column(name="createdBy")
	public Long createdBy;

	@Column(name="updatedBy")
	public Long updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	public Date deletedAt;

	public Resource() {

	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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


