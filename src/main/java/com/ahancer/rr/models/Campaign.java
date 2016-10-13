package com.ahancer.rr.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ahancer.rr.custom.type.CampaignStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity(name="campaign")
public class Campaign extends AbstractModel implements Serializable {

	private static final long serialVersionUID = 3194504854142231694L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long campaignId;
	
	@Column(name="brandId")
	private Long brandId;

	@MapsId("brandId")
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE)
	@JoinColumn(name="brandId")
	private Brand brand;
	
	@Column(name="title",length=255)
	private String title;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="objectiveId")
	private CampaignObjective objective;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="campaignWorkType",
			joinColumns=@JoinColumn(name="campaignId", referencedColumnName="campaignId"),
			inverseJoinColumns=@JoinColumn(name="workTypeId", referencedColumnName="workTypeId"))
	@OrderBy("workTypeId")
	private Set<WorkType> workType = new HashSet<WorkType>(0);
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="campaignMedia",
			joinColumns=@JoinColumn(name="campaignId", referencedColumnName="campaignId"),
			inverseJoinColumns=@JoinColumn(name="mediaId", referencedColumnName="mediaId"))
	@OrderBy("mediaId")
	private Set<Media> media = new HashSet<Media>(0);
	
	@Lob
	@Column(name="description")
	private String description;
	
	@Column(name="productName",length=255)
	private String productName;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE)
	@JoinColumn(name="categoryId")
	private Category category;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="mainResourceId")
	private Resource mainResource;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "campaign")
	@OrderBy("position")
	private Set<CampaignResource> campaignResources = new HashSet<CampaignResource>(0);
	
	@Column(name="website",length=255)
	private String website;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE)
	@JoinColumn(name="budgetId")
	private Budget budget;

	@Temporal(TemporalType.TIMESTAMP)
	private Date proposalDeadline;

	@Column(name="status",length=20)
	@Enumerated(EnumType.STRING)
	private CampaignStatus status;
	
	@Column(name="rabbitFlag")
	private Boolean rabbitFlag;
	
	@JsonIgnore
	@OneToMany(mappedBy="campaign",fetch=FetchType.EAGER)
	private Set<Proposal> proposals = new HashSet<Proposal>(0);
	
	@Column(name="publicCode",length=32)
	private String publicCode;

	public Campaign() {

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

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CampaignObjective getObjective() {
		return objective;
	}

	public void setObjective(CampaignObjective objective) {
		this.objective = objective;
	}

	public Set<WorkType> getWorkType() {
		return workType;
	}

	public void setWorkType(Set<WorkType> workType) {
		this.workType = workType;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
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
