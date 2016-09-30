package com.ahancer.rr.response;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.ahancer.rr.custom.type.CampaignStatus;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Budget;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.CampaignResource;
import com.ahancer.rr.models.Category;
import com.ahancer.rr.models.Media;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.Resource;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CampaignResponse implements Serializable {

	private static final long serialVersionUID = -6548467101012096258L;
	private Long campaignId;
	private Long brandId;
	private BrandResponse brand;
	private Category category;
	private Budget budget;
	private Set<Media> media = new HashSet<Media>(0);
	private Resource mainResource;
	private Set<CampaignResource> campaignResources = new HashSet<CampaignResource>(0);
	private String keyword;
	private String title;
	private String description;
	private String website;
	private Date proposalDeadline;
	private CampaignStatus status;
	private Boolean rabbitFlag;
	private Integer countProposalSelection;
	private Integer countProposalWorking;
	private Integer countProposalComplete;
	private Date updatedAt;
	private Boolean isApply;
	private String publicCode;
	@JsonIgnore
	private Set<Proposal> proposals = new HashSet<Proposal>(0);
	private Proposal proposal;

	public CampaignResponse() {
		
	}
	
	public CampaignResponse(Campaign campaign, String roleValue) {
		Role role = Role.valueOf(roleValue);
		switch (role) {
			case Admin:
				updatedAt = campaign.getUpdatedAt();
				Iterator<Proposal> iterate = campaign.getProposals().iterator();
				this.countProposalSelection = 0;
				this.countProposalWorking = 0;
				this.countProposalComplete = 0;
				while(iterate.hasNext()){
					switch(iterate.next().getStatus()){
						case Selection:
							++countProposalSelection;
							break;
						case Working:
							++countProposalWorking;
							break;
						case Complete:
							++countProposalComplete;
							break;
						default:
							break;
					}
				}
				break;
			case Brand:
				break;
			case Influencer:
				this.proposals = campaign.getProposals();
				break;
			default:
				break;
		}
		this.brand = new BrandResponse(campaign.getBrand(),roleValue);
		this.campaignId = campaign.getCampaignId();
		this.brandId = campaign.getBrandId();
		this.category = campaign.getCategory();
		this.budget = campaign.getBudget();
		this.media = campaign.getMedia();
		this.mainResource = campaign.getMainResource();
		this.campaignResources = campaign.getCampaignResources();
		this.keyword = campaign.getKeyword();
		this.title = campaign.getTitle();
		this.description = campaign.getDescription();
		this.website = campaign.getWebsite();
		this.proposalDeadline = campaign.getProposalDeadline();
		this.status = campaign.getStatus();
		this.rabbitFlag = campaign.getRabbitFlag();
		this.publicCode = campaign.getPublicCode();
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public BrandResponse getBrand() {
		return brand;
	}

	public void setBrand(BrandResponse brand) {
		this.brand = brand;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public Set<Media> getMedia() {
		return media;
	}

	public void setMedia(Set<Media> media) {
		this.media = media;
	}

	public Resource getMainResource() {
		return mainResource;
	}

	public void setMainResource(Resource mainResource) {
		this.mainResource = mainResource;
	}

	public Set<CampaignResource> getCampaignResources() {
		return campaignResources;
	}

	public void setCampaignResources(Set<CampaignResource> campaignResources) {
		this.campaignResources = campaignResources;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Date getProposalDeadline() {
		return proposalDeadline;
	}

	public void setProposalDeadline(Date proposalDeadline) {
		this.proposalDeadline = proposalDeadline;
	}

	public CampaignStatus getStatus() {
		return status;
	}

	public void setStatus(CampaignStatus status) {
		this.status = status;
	}

	public Boolean getRabbitFlag() {
		return rabbitFlag;
	}

	public void setRabbitFlag(Boolean rabbitFlag) {
		this.rabbitFlag = rabbitFlag;
	}

	public Integer getCountProposalSelection() {
		return countProposalSelection;
	}

	public void setCountProposalSelection(Integer countProposalSelection) {
		this.countProposalSelection = countProposalSelection;
	}

	public Integer getCountProposalWorking() {
		return countProposalWorking;
	}

	public void setCountProposalWorking(Integer countProposalWorking) {
		this.countProposalWorking = countProposalWorking;
	}

	public Integer getCountProposalComplete() {
		return countProposalComplete;
	}

	public void setCountProposalComplete(Integer countProposalComplete) {
		this.countProposalComplete = countProposalComplete;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Boolean getIsApply() {
		return isApply;
	}

	public void setIsApply(Boolean isApply) {
		this.isApply = isApply;
	}
	
	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public Set<Proposal> getProposals() {
		return proposals;
	}

	public void setProposals(Set<Proposal> proposals) {
		this.proposals = proposals;
	}

	public String getPublicCode() {
		return publicCode;
	}

	public void setPublicCode(String publicCode) {
		this.publicCode = publicCode;
	}
	
	
}
