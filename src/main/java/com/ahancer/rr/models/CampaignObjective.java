package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="campaignObjective")
public class CampaignObjective implements Serializable {
	
	private static final long serialVersionUID = 6562672041351520882L;

	@Id
	@Column(name="objectiveId",unique = true, nullable = false)
	private Long objectiveId;
	
	@Column(name="objectiveName",length=255)
	private String objectiveName;
	
	@JsonIgnore
	@Column(name="isActive")
	private Boolean isActive;
	
	public CampaignObjective(){
		
	}

	public Long getObjectiveId() {
		return objectiveId;
	}

	public void setObjectiveId(Long objectiveId) {
		this.objectiveId = objectiveId;
	}

	public String getObjectiveName() {
		return objectiveName;
	}

	public void setObjectiveName(String objectiveName) {
		this.objectiveName = objectiveName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
}
