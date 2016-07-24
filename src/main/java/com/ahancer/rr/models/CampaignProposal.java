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

@Entity(name="campaignProposal")
public class CampaignProposal {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long proposalId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="influencerId",nullable=false)
	private Influencer influencer;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="campaignId",nullable=false)
	private Influencer campaign;
	
	@Column(name="title",length=255)
	private String title;
	
	@Column(name="description",length=255)
	private String description;
	
	@Column(name="comment",length=255)
	private String comment;
	
	
	@Column(name="proposePrice",scale=10,precision=3)
	private Double proposePrice;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="CampaignProposalResource",
			joinColumns=@JoinColumn(name="proposalId", referencedColumnName="proposalId"),
			inverseJoinColumns=@JoinColumn(name="resourceId", referencedColumnName="resourceId"))
	private List<Resource> resources;
	
	@Column(name="isSelected",length=255)
	private Boolean isSelected;
	
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

	public Long getProposalId() {
		return proposalId;
	}

	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Double getProposePrice() {
		return proposePrice;
	}

	public void setProposePrice(Double proposePrice) {
		this.proposePrice = proposePrice;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
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
