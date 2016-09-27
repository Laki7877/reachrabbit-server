package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.custom.type.ResourceType;

@Entity(name="resource")
public class Resource extends AbstractModel implements Serializable{
	
	private static final long serialVersionUID = -8947832737382539230L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long resourceId;

	@Column(name="resourcePath",length=255)
	private String resourcePath;

	@Enumerated(EnumType.STRING)
	private ResourceType resourceType;
	
	public Resource() {

	}
	public String getUrl() {
		return "https://" + ApplicationConstant.Bucket +".s3-ap-southeast-1.amazonaws.com/" + this.getResourcePath();
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

}


