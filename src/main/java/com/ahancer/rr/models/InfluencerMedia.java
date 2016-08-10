package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.springframework.data.annotation.Id;

@Entity(name="influencerMedia")
public class InfluencerMedia implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6589347514308393340L;
	
	@Id
	@Column(name="influencerId", nullable=false)
	private Long influencerId;
	
	@Id
	@Column(name="mediaId", nullable=false)
	private String mediaId;
	
	@Column(name="socialId", nullable=false)
	private String socialId;

	@MapsId("mediaId")
	@ManyToOne
	@JoinColumn(name="mediaId")
	private Media media;
	
	@MapsId("influencerId")
	@ManyToOne
	@JoinColumn(name="influencerId")
	private Influencer influencer;
	
	public Long getInfluencerId() {
		return influencerId;
	}
	public void setInfluencerId(Long influencerId) {
		this.influencerId = influencerId;
	}
	
	public String getSocialId() {
		return socialId;
	}
	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public Media getMedia() {
		return media;
	}
	public void setMedia(Media media) {
		this.media = media;
	}
	public Influencer getInfluencer() {
		return influencer;
	}
	public void setInfluencer(Influencer influencer) {
		this.influencer = influencer;
	}
	
	
}
