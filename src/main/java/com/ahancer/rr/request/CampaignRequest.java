package com.ahancer.rr.request;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Size;

import com.ahancer.rr.custom.type.CampaignStatus;
import com.ahancer.rr.models.Budget;
import com.ahancer.rr.models.CampaignResource;
import com.ahancer.rr.models.Category;
import com.ahancer.rr.models.Media;
import com.ahancer.rr.models.Resource;

public class CampaignRequest implements Serializable {
	private static final long serialVersionUID = 1164567255480906846L;
	
	private Long campaignId;
	
	private Budget budget;
	
	@Size(max=200,message="error.campaign.website.length")
	private String website;
	
	private Date proposalDeadline;
	
	private CampaignStatus status;
	
	@Size(max=5000,message="error.campaign.website.length")
	private String description;
	
	@Size(max=100,message="error.campaign.title.length")
	private String title;
	
	
	private Category category;
	
	private Set<Media> media = new HashSet<Media>(0);
	
	private Resource mainResource;
	
	private Set<CampaignResource> campaignResources = new HashSet<CampaignResource>(0);
	
	public CampaignRequest() {
	}

	public Budget getBudget() {
		return budget;
	}
	public void setBudget(Budget budget) {
		this.budget = budget;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Set<Media> getMedia() {
		return media;
	}
	public void setMedia(Set<Media> media) {
		this.media = media;
	}
	public Set<CampaignResource> getCampaignResources() {
		return campaignResources;
	}
	public void setCampaignResources(Set<CampaignResource> campaignResources) {
		this.campaignResources = campaignResources;
	}
	public Resource getMainResource() {
		return mainResource;
	}
	public void setMainResource(Resource mainResource) {
		this.mainResource = mainResource;
	}
	public Long getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	
}
