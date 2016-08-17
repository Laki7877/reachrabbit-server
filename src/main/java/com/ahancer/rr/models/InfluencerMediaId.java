package com.ahancer.rr.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class InfluencerMediaId implements java.io.Serializable {

	private static final long serialVersionUID = 6974949989812188922L;
	
	@Column(name = "influencerId", nullable = false)
	private long influencerId;
	
	@Column(name = "mediaId", nullable = false)
	private String mediaId;

	public InfluencerMediaId() {
	}

	public InfluencerMediaId(long influencerId, String mediaId) {
		this.influencerId = influencerId;
		this.mediaId = mediaId;
	}

	
	public long getInfluencerId() {
		return this.influencerId;
	}

	public void setInfluencerId(long influencerId) {
		this.influencerId = influencerId;
	}


	public String getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof InfluencerMediaId))
			return false;
		InfluencerMediaId castOther = (InfluencerMediaId) other;

		return (this.getInfluencerId() == castOther.getInfluencerId())
				&& ((this.getMediaId() == castOther.getMediaId()) || (this.getMediaId() != null
						&& castOther.getMediaId() != null && this.getMediaId().equals(castOther.getMediaId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getInfluencerId();
		result = 37 * result + (getMediaId() == null ? 0 : this.getMediaId().hashCode());
		return result;
	}

}
