package com.ahancer.rr.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ahancer.rr.custom.type.CampaignStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="campaign")
public class Campaign implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3194504854142231694L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long campaignId;
	
	@Column(name="brandId",unique = true, nullable = false)
	private Long brandId;

	@MapsId("brandId")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="brandId",nullable=false)
	private Brand brand;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="categoryId",nullable=false)
	private Category category;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="CampaignMedia",
			joinColumns=@JoinColumn(name="campaignId", referencedColumnName="campaignId"),
			inverseJoinColumns=@JoinColumn(name="mediaId", referencedColumnName="mediaId"))
	private List<Media> media;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="CampaignResource",
			joinColumns=@JoinColumn(name="campaignId", referencedColumnName="campaignId"),
			inverseJoinColumns=@JoinColumn(name="resourceId", referencedColumnName="resourceId"))
	private List<Resource> resources;

	@Column(name="title",length=255)
	private String title;

	@Column(name="description",length=255)
	private String description;


	@Temporal(TemporalType.TIMESTAMP)
	private Date proposalDeadline;

	@Temporal(TemporalType.TIMESTAMP)
	private Date submissionDeadline;

	@Column(name="status",length=20)
	@Enumerated(EnumType.STRING)
	private CampaignStatus status;

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

	public Campaign() {

	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getProposalDeadline() {
		return proposalDeadline;
	}

	public void setProposalDeadline(Date proposalDeadline) {
		this.proposalDeadline = proposalDeadline;
	}

	public Date getSubmissionDeadline() {
		return submissionDeadline;
	}

	public void setSubmissionDeadline(Date submissionDeadline) {
		this.submissionDeadline = submissionDeadline;
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

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public List<Media> getMedia() {
		return media;
	}

	public void setMedia(List<Media> media) {
		this.media = media;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public CampaignStatus getStatus() {
		return status;
	}

	public void setStatus(CampaignStatus status) {
		this.status = status;
	}


}
