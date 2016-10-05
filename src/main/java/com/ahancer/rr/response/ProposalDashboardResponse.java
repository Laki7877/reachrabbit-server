package com.ahancer.rr.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.models.Influencer;

public class ProposalDashboardResponse implements Serializable {

	private static final long serialVersionUID = -1877962983132486529L;
	
	private Long proposalId;
	private Long influencerId;
	private Influencer influencer;
	private Double price;
	private ProposalStatus status;
	private List<PostAggregateResponse> posts = new ArrayList<PostAggregateResponse>(0);
	
	public ProposalDashboardResponse() { 
		
	}

	public ProposalDashboardResponse(Long proposalId, Long influencerId, Influencer influencer,
			Double price, ProposalStatus status) {
		this.proposalId = proposalId;
		this.influencerId = influencerId;
		this.influencer = influencer;
		this.price = price;
		this.status = status;
	}

	public Long getProposalId() {
		return proposalId;
	}

	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
	}

	public Long getInfluencerId() {
		return influencerId;
	}

	public void setInfluencerId(Long influencerId) {
		this.influencerId = influencerId;
	}

	public Influencer getInfluencer() {
		return influencer;
	}

	public void setInfluencer(Influencer influencer) {
		this.influencer = influencer;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public ProposalStatus getStatus() {
		return status;
	}

	public void setStatus(ProposalStatus status) {
		this.status = status;
	}

	public List<PostAggregateResponse> getPosts() {
		return posts;
	}

	public void setPosts(List<PostAggregateResponse> posts) {
		this.posts = posts;
	}
}
