package com.ahancer.rr.models;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ahancer.rr.custum.type.ProposalStatus;

@Entity(name="campaignSubmission")
public class CampaignSubmission {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long submissionId;
	
	@Column(name="title",length=255)
	private String title;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="influencerId",nullable=false)
	private Influencer influencer;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="campaignId",nullable=false)
	private Influencer campaign;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="CampaignSubmissionResource",
			joinColumns=@JoinColumn(name="submissionId", referencedColumnName="submissionId"),
			inverseJoinColumns=@JoinColumn(name="resourceId", referencedColumnName="resourceId"))
	private List<Resource> resources;
	
	@Column(name="status",length=20)
	@Enumerated(EnumType.STRING)
	private ProposalStatus status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name="createdBy")
	private Long createdBy;
	
	@Column(name="updatedBy")
	private Long updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedAt;
	
	public CampaignSubmission() {
		
	}

	public Long getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Influencer getInfluencer() {
		return influencer;
	}

	public void setInfluencer(Influencer influencer) {
		this.influencer = influencer;
	}

	public Influencer getCampaign() {
		return campaign;
	}

	public void setCampaign(Influencer campaign) {
		this.campaign = campaign;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public ProposalStatus getStatus() {
		return status;
	}

	public void setStatus(ProposalStatus status) {
		this.status = status;
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
