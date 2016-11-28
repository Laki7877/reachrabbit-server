package com.ahancer.rr.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="post")
public class Post extends AbstractModel implements Serializable {
	
	private static final long serialVersionUID = -5556110403033071026L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long postId;
	
	@Column(name="socialPostId",length=255)
	private String socialPostId;
	
	@Column(name="proposalId",nullable=false)
	private Long proposalId;
	
	@JsonIgnore
	@MapsId("proposalId")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="proposalId",nullable=false)
	private Proposal proposal;
	
	@Column(name="mediaId",nullable=false)
	private String mediaId;
	
	@MapsId("mediaId")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="mediaId",nullable=false)
	private Media media;
	
	@Column(name="likeCount")
	private Long likeCount;
	
	@Column(name="shareCount")
	private Long shareCount;
	
	@Column(name="commentCount")
	private Long commentCount;
	
	@Column(name="viewCount")
	private Long viewCount;
	
	
	@Temporal(TemporalType.DATE)
	@Column(name="dataDate")
	private Date dataDate;
	
	@Column(name="isPersonalAccountPost")
	private Boolean isPersonalAccountPost;
	
	@Column(name="url")
	private String url;

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getSocialPostId() {
		return socialPostId;
	}

	public void setSocialPostId(String socialPostId) {
		this.socialPostId = socialPostId;
	}

	public Long getProposalId() {
		return proposalId;
	}

	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
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

	public Long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Long likeCount) {
		this.likeCount = likeCount;
	}

	public Long getShareCount() {
		return shareCount;
	}

	public void setShareCount(Long shareCount) {
		this.shareCount = shareCount;
	}

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getDataDate() {
		return dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}

	public Boolean getIsPersonalAccountPost() {
		return isPersonalAccountPost;
	}

	public void setIsPersonalAccountPost(Boolean isPersonalAccountPost) {
		this.isPersonalAccountPost = isPersonalAccountPost;
	}
	
}
