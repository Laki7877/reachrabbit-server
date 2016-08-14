package com.ahancer.rr.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ahancer.rr.custom.type.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="influencer")
public class Influencer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2725911649000100273L;
	
	@Id
	@Column(name="influencerId",unique = true, nullable = false)
	private Long influencerId;

	@JsonIgnore
	@MapsId("influencerId")
	@OneToOne
    @JoinColumn(name = "influencerId")
	private User user;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(name="web",length=255)
	private String web;

	@Column(name="about",length=255)
	private String about;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="birthday")
	private Date birthday;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "influencer")
	private List<InfluencerMedia> influencerMedias;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="InfluencerCategory",
			joinColumns=@JoinColumn(name="influencerId", referencedColumnName="influencerId"),
			inverseJoinColumns=@JoinColumn(name="categoryId", referencedColumnName="categoryId"))
	private List<Category> categories;

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


	public Influencer() {

	}

	public Long getInfluencerId() {
		return influencerId;
	}

	public void setInfluencerId(Long influencerId) {
		this.influencerId = influencerId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
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

	public List<InfluencerMedia> getInfluencerMedias() {
		return influencerMedias;
	}

	public void setInfluencerMedias(List<InfluencerMedia> influencerMedias) {
		this.influencerMedias = influencerMedias;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

}
