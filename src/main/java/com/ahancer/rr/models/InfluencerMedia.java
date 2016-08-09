package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.Id;

@Entity(name="influencerMedia")
public class InfluencerMedia implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6589347514308393340L;
	
	@Id
	private Long influencerId;
	
	@Id
	private Long mediaId;
	
	@Column(name="socialId", nullable=false)
	private String socialId;

	@ManyToOne
	@JoinColumn(name="influencerId")
	private Media media;
	
	@ManyToOne
	@JoinColumn(name="mediaId")
	private Influencer influencer;
	
	public Long getInfluencerId() {
		return influencerId;
	}
	public void setInfluencerId(Long influencerId) {
		this.influencerId = influencerId;
	}
	public Long getMediaId() {
		return mediaId;
	}
	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}
	public String getSocialId() {
		return socialId;
	}
	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}
	
	
}
