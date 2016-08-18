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

@Entity(name="brand")
public class Brand extends AbstractModel implements Serializable {

	private static final long serialVersionUID = -6103774649322540379L;

	@Id
	@Column(name="brandId",unique = true, nullable = false)
	private Long brandId;

	@MapsId("brandId")
	@OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "brandId")
//	@JsonBackReference
	@JsonManagedReference(value="user-brand")
	private User user;

	@Column(name="brandName",length=255)
	private String brandName;
	
	@Column(name="about",length=255)
	private String about;
	
	@Column(name="website",length=255)
	private String website;

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
