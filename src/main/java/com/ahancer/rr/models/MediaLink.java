package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity(name="mediaLink")
public class MediaLink implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5461611858809955253L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long linkId;
	
	@Column(name="socialId")
	private String socialId;
	
	@Column(name="pageId")
	private String pageId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="mediaId")
	private Media media;

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

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
}
