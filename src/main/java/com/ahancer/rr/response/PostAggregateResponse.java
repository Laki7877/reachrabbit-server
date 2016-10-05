package com.ahancer.rr.response;

import java.io.Serializable;
import java.util.Date;

public class PostAggregateResponse implements Serializable {
	
	private static final long serialVersionUID = 5476915167310300515L;
	private Date date;
	private String mediaId;
	private Long sumLike;
	private Long sumComment;
	private Long sumView;
	private Long sumShare;
	public PostAggregateResponse() {
		
	}
	public PostAggregateResponse(Date date, String mediaId, Long sumLike, Long sumComment, Long sumView,
			Long sumShare) {
		super();
		this.date = date;
		this.mediaId = mediaId;
		this.sumLike = sumLike;
		this.sumComment = sumComment;
		this.sumView = sumView;
		this.sumShare = sumShare;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public Long getSumLike() {
		return sumLike;
	}
	public void setSumLike(Long sumLike) {
		this.sumLike = sumLike;
	}
	public Long getSumComment() {
		return sumComment;
	}
	public void setSumComment(Long sumComment) {
		this.sumComment = sumComment;
	}
	public Long getSumView() {
		return sumView;
	}
	public void setSumView(Long sumView) {
		this.sumView = sumView;
	}
	public Long getSumShare() {
		return sumShare;
	}
	public void setSumShare(Long sumShare) {
		this.sumShare = sumShare;
	}
}
