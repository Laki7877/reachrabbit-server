package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity(name="mediaLink")
public class MediaLink implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5461611858809955253L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long linkId;
	
	@Column(name="mediaId")
	private String mediaId;
	
	@Column(name="socialId")
	private String socialId;
	
	@MapsId
	@OneToOne
	@JoinColumn(name="mediaId")
	private Media media;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public Long getLinkId() {
		return linkId;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}
}
