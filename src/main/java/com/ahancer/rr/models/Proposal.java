package com.ahancer.rr.models;

import java.io.Serializable;
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

import com.ahancer.rr.custom.type.ProposalStatus;

@Entity(name="proposal")
public class Proposal extends AbstractModel implements Serializable{
	
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
			name="proposalMedia",
			joinColumns=@JoinColumn(name="proposalId", referencedColumnName="proposalId"),
			inverseJoinColumns=@JoinColumn(name="mediaId", referencedColumnName="mediaId"))
	private Set<Media> media = new HashSet<Media>(0);
	
	@Column(name="description",length=255)
	private String description;
	
	@Column(name="proposePrice",scale=10,precision=3)
	private Double proposePrice;
	
	@Column(name="proposeFee",scale=10,precision=3)
	private Double proposeFee;
	
	@Column(name="status",length=20)
	@Enumerated(EnumType.STRING)
	private ProposalStatus status;
	
	public Proposal() {
		
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

	public Double getProposeFee() {
		return proposeFee;
	}

	public void setProposeFee(Double proposeFee) {
		this.proposeFee = proposeFee;
	}
	
}
