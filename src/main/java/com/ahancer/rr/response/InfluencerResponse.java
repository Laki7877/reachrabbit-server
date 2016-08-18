package com.ahancer.rr.response;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.ahancer.rr.custom.type.Gender;
import com.ahancer.rr.models.Category;
import com.ahancer.rr.models.InfluencerMedia;

public class InfluencerResponse implements Serializable {

	private static final long serialVersionUID = 3006172183699657419L;
	private Long influencerId;
	private Gender gender;
	private String web;
	private String about;
	private Date birthday;
	private Set<InfluencerMedia> influencerMedias = new HashSet<InfluencerMedia>(0);
	private Set<Category> categories = new HashSet<Category>(0);
	
	public InfluencerResponse(){
		
	}

	public Long getInfluencerId() {
		return influencerId;
	}

	public void setInfluencerId(Long influencerId) {
		this.influencerId = influencerId;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Set<InfluencerMedia> getInfluencerMedias() {
		return influencerMedias;
	}

	public void setInfluencerMedias(Set<InfluencerMedia> influencerMedias) {
		this.influencerMedias = influencerMedias;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
}
