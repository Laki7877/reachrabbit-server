package com.ahancer.rr.response;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class FacebookProfileResponse implements Serializable{

	/**
	 * 
	 */
	public static class Post implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String picture;
		private String video;
		private String videoEmbedded;
		private String link;
		private String message;
		
		private BigInteger likes;
		private BigInteger shares;
		private BigInteger comments;
		
		public String getPicture() {
			return picture;
		}
		public void setPicture(String picture) {
			this.picture = picture;
		}
		
		public String getVideo() {
			return video;
		}
		public void setVideo(String video) {
			this.video = video;
		}
		public String getVideoEmbedded() {
			return videoEmbedded;
		}
		public void setVideoEmbedded(String videoEmbedded) {
			this.videoEmbedded = videoEmbedded;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public BigInteger getLikes() {
			return likes;
		}
		public void setLikes(BigInteger likes) {
			this.likes = likes;
		}
		public BigInteger getShares() {
			return shares;
		}
		public void setShares(BigInteger shares) {
			this.shares = shares;
		}
		public BigInteger getComments() {
			return comments;
		}
		public void setComments(BigInteger comments) {
			this.comments = comments;
		}
		
		
	}
	private static final long serialVersionUID = 1L;
	private String name;
	private String id;
	private BigInteger likes;
	private String picture;
	private String link;
	
	private BigInteger averageLikes;
	private BigInteger averageComments;
	private BigInteger averageShares;
	
	private List<Post> posts;
	
	
	public BigInteger getAverageLikes() {
		return averageLikes;
	}
	public void setAverageLikes(BigInteger averageLikes) {
		this.averageLikes = averageLikes;
	}
	public BigInteger getAverageComments() {
		return averageComments;
	}
	public void setAverageComments(BigInteger averageComments) {
		this.averageComments = averageComments;
	}
	public BigInteger getAverageShares() {
		return averageShares;
	}
	public void setAverageShares(BigInteger averageShares) {
		this.averageShares = averageShares;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigInteger getLikes() {
		return likes;
	}
	public void setLikes(BigInteger likes) {
		this.likes = likes;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public List<Post> getPosts() {
		return posts;
	}
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
}
