package com.ahancer.rr.response;

import java.io.Serializable;

import com.ahancer.rr.models.Media;

public class UpdatePostResponse implements Serializable {
	
	private static final long serialVersionUID = 3399282791877787947L;
	
	private String socialPostId;
	private String mediaId;
	private Media media;
	private Long proposalId;
	private String url;
	
	
	public UpdatePostResponse(String socialPostId, String mediaId, Long proposalId, String url){
		this.socialPostId = socialPostId;
		this.mediaId = mediaId;
		this.proposalId = proposalId;
		this.url = url;
	}
	
	
	public UpdatePostResponse(String socialPostId, String mediaId, Long proposalId, String url, Media media){
		this.socialPostId = socialPostId;
		this.mediaId = mediaId;
		this.proposalId = proposalId;
		this.url = url;
		this.media = media;
	}

	public String getSocialPostId() {
		return socialPostId;
	}

	public void setSocialPostId(String socialPostId) {
		this.socialPostId = socialPostId;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public Long getProposalId() {
		return proposalId;
	}

	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}
}
