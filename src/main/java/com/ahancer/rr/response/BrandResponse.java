package com.ahancer.rr.response;

import java.io.Serializable;

public class BrandResponse implements Serializable {

	private static final long serialVersionUID = 4549371860913222596L;
	private Long brandId;
	private String brandName;
	private String about;
	private String website;
	
	public BrandResponse(){
		
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
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
