package com.ahancer.rr.models;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "campaignResource")
public class CampaignResource implements java.io.Serializable {

	private static final long serialVersionUID = 5411958738292073352L;
	
	@JsonIgnore
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "campaignId", column = @Column(name = "campaignId", nullable = false)),
			@AttributeOverride(name = "resourceId", column = @Column(name = "resourceId", nullable = false)) })
	private CampaignResourceId id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaignId", nullable = false, insertable = false, updatable = false)
	private Campaign campaign;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "resourceId", nullable = false, insertable = false, updatable = false)
	private Resource resource;
	
	@Column(name = "position")
	private Integer position;

	public CampaignResource() {
	}
	
	public CampaignResourceId getId() {
		return this.id;
	}

	public void setId(CampaignResourceId id) {
		this.id = id;
	}
	
	public Campaign getCampaign() {
		return this.campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public Resource getResource() {
		return this.resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public Integer getPosition() {
		return this.position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

}
