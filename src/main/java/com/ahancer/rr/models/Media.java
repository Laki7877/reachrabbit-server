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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long mediaId;

	@Column(name="mediaName",length=255)
	private String mediaName;

	public Media() {

	}

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}


}
