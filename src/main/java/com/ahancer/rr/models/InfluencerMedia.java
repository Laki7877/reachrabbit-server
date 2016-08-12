package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="influencerMedia")
public class InfluencerMedia implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8946159458429934385L;

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "influencerId", column = @Column(name = "influencerId", nullable = false)),
			@AttributeOverride(name = "mediaId", column = @Column(name = "mediaId", nullable = false)) })
	private InfluencerMediaId influencerMediaId;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "influencerId", nullable = false, insertable = false, updatable = false)
	private Influencer influencer;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mediaId", nullable = false, insertable = false, updatable = false)
	private Media media;
	
	@Column(name="socialId")
	private String socialId;
	
	@Column(name="pageId")
	private String pageId;
	
	public InfluencerMedia() {
		
	}

	public InfluencerMediaId getInfluencerMediaId() {
		return influencerMediaId;
	}

	public void setInfluencerMediaId(InfluencerMediaId influencerMediaId) {
		this.influencerMediaId = influencerMediaId;
	}

	public Influencer getInfluencer() {
		return influencer;
	}

	public void setInfluencer(Influencer influencer) {
		this.influencer = influencer;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	
	
}
