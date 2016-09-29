package com.ahancer.rr.response;

import java.io.Serializable;

public class UpdatePostResponse implements Serializable {
	
	private static final long serialVersionUID = 3399282791877787947L;
	
	private String socialPostId;
	private String mediaId;
	private Long proposalId;
	
	public UpdatePostResponse(String socialPostId, String mediaId, Long proposalId){
		this.socialPostId = socialPostId;
		this.mediaId = mediaId;
		this.proposalId = proposalId;
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
}
