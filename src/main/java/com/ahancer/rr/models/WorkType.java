package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="workType")
public class WorkType implements Serializable{

	private static final long serialVersionUID = 3411823077503411945L;
	
	@Id
	@Column(name="workTypeId",unique = true, nullable = false)
	private Long workTypeId;
	
	@Column(name="workTypeName",length=255)
	private String workTypeName;
	
	@Column(name="isActive")
	private Boolean isActive;
	
	public WorkType() {
		
	}

	public Long getWorkTypeId() {
		return workTypeId;
	}

	public void setWorkTypeId(Long workTypeId) {
		this.workTypeId = workTypeId;
	}

	public String getWorkTypeName() {
		return workTypeName;
	}

	public void setWorkTypeName(String workTypeName) {
		this.workTypeName = workTypeName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
