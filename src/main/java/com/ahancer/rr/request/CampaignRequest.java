package com.ahancer.rr.request;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.ahancer.rr.custom.type.CampaignStatus;
import com.ahancer.rr.models.CampaignResource;
import com.ahancer.rr.models.Category;
import com.ahancer.rr.models.Media;
import com.ahancer.rr.models.Resource;

public class CampaignRequest implements Serializable {
	private static final long serialVersionUID = 1164567255480906846L;
	private Double fromBudget;
	private Double toBudget;
	private String website;
	private Date proposalDeadline;
	private CampaignStatus status;
	private String description;
	private String title;
	private String keyword;
	private Category category;
	private Set<Media> media = new HashSet<Media>(0);
	private Resource mainResource;
	private Set<CampaignResource> campaignResources = new HashSet<CampaignResource>(0);
	public CampaignRequest() {
	}
	public CampaignRequest(Double fromBudget, Double toBudget, String website, Date proposalDeadline,
			CampaignStatus status, String description, String title, String keyword, Category category,
			Set<Media> media, Set<CampaignResource> campaignResources,Resource mainResource) {
		super();
		this.fromBudget = fromBudget;
		this.toBudget = toBudget;
		this.website = website;
		this.proposalDeadline = proposalDeadline;
		this.status = status;
		this.description = description;
		this.title = title;
		this.keyword = keyword;
		this.category = category;
		this.media = media;
		this.campaignResources = campaignResources;
		this.mainResource = mainResource;
	}
	public Double getFromBudget() {
		return fromBudget;
	}
	public void setFromBudget(Double fromBudget) {
		this.fromBudget = fromBudget;
	}
	public Double getToBudget() {
		return toBudget;
	}
	public void setToBudget(Double toBudget) {
		this.toBudget = toBudget;
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
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
}
