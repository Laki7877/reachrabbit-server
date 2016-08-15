package com.ahancer.rr.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

import com.ahancer.rr.custom.type.ProposalStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="campaignProposalRevision")
public class CampaignProposalRevision implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8215908401135233334L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long revisionlId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="proposalId",nullable=false)
	private CampaignProposal proposal;
	
	
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
			name="CampaignProposalRevisionResource",
			joinColumns=@JoinColumn(name="revisionlId", referencedColumnName="revisionlId"),
			inverseJoinColumns=@JoinColumn(name="resourceId", referencedColumnName="resourceId"))
	private Set<Resource> resources = new HashSet<Resource>(0);
	
	@Column(name="isSelected",length=255)
	private Boolean isSelected;
	
	@Column(name="status",length=20)
	@Enumerated(EnumType.STRING)
	private ProposalStatus status;
	
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

	public Long getRevisionlId() {
		return revisionlId;
	}

	public void setRevisionlId(Long revisionlId) {
		this.revisionlId = revisionlId;
	}

	public CampaignProposal getProposal() {
		return proposal;
	}

	public void setProposal(CampaignProposal proposal) {
		this.proposal = proposal;
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

	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
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
