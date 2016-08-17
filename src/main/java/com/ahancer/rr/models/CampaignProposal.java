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

@Entity(name="campaignProposal")
public class CampaignProposal implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2295333503977165543L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long proposalId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="influencerId",nullable=false)
	private Influencer influencer;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="campaignId",nullable=false)
	private Campaign campaign;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="completionId")
	private CompletionTime completionTime;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="CampaignProposalMedia",
			joinColumns=@JoinColumn(name="proposalId", referencedColumnName="proposalId"),
			inverseJoinColumns=@JoinColumn(name="mediaId", referencedColumnName="mediaId"))
	private Set<Media> media = new HashSet<Media>(0);
	
	@Column(name="description",length=255)
	private String description;
	
	@Column(name="proposePrice",scale=10,precision=3)
	private Double proposePrice;
	
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
	
	public CampaignProposal() {
		
	}

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

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public CompletionTime getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(CompletionTime completionTime) {
		this.completionTime = completionTime;
	}

	public Set<Media> getMedia() {
		return media;
	}

	public void setMedia(Set<Media> media) {
		this.media = media;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getProposePrice() {
		return proposePrice;
	}

	public void setProposePrice(Double proposePrice) {
		this.proposePrice = proposePrice;
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
