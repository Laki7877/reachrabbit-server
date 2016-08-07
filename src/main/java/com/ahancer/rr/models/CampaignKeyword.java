package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="campaignKeyword")
public class CampaignKeyword implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5422743617089833631L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long keywordId;
	
	@Column(name="keyword",length=30)
	private String keyword;
	
	
//	@Column(name="campaignId")
//	private Long campaignId;
	
	
	@JsonIgnore
	//@MapsId("campaignId")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="campaignId", nullable = false)
	private Campaign campaign;
	
	public CampaignKeyword() {
		
	}

	public Long getKeywordId() {
		return keywordId;
	}

	public void setKeywordId(Long keywordId) {
		this.keywordId = keywordId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

//	public Long getCampaignId() {
//		return campaignId;
//	}
//
//	public void setCampaignId(Long campaignId) {
//		this.campaignId = campaignId;
//	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	

}


