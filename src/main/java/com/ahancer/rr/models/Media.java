package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="media")
public class Media implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7106394972853750323L;

	@Id
	@Column(length=255)
	private String mediaId;

	@Column(name="mediaName",length=255)
	private String mediaName;
	
	@Column(name="isActive")
	private boolean isActive;

	public Media() {

	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}


}
