package com.ahancer.rr.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="brand")
public class Brand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6103774649322540379L;

	@Id
	@Column(name="brandId",unique = true, nullable = false)
	private Long brandId;

	@MapsId("brandId")
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "brandId")
	private User user;

	@Column(name="brandName",length=255)
	private String brandName;
	
	@Column(name="about",length=255)
	private String about;
	
	@Column(name="website",length=255)
	private String website;


	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@JsonIgnore
	@Column(name="createdBy")
	private Long createdBy;

	@JsonIgnore
	@Column(name="updatedBy")
	private Long updatedBy;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedAt;


	public Brand() {

	}


	public Long getBrandId() {
		return brandId;
	}


	public void setBrandId(Long brandId) {
		this.brandId = brandId;
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


	public String getAbout() {
		return about;
	}


	public void setAbout(String about) {
		this.about = about;
	}


	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}

	
}
