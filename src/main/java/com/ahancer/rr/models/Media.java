package com.ahancer.rr.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity(name="media")
public class Media {

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
