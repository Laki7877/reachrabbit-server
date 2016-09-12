package com.ahancer.rr.response;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class InstagramProfileResponse implements Serializable {

	/**
	 * 
	 */
	public static class Post implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String type;
		
		private String video;
		private String image;
		private Integer width;
		private Integer height; //video width height
		
		private BigInteger likes;
		private BigInteger comments;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getVideo() {
			return video;
		}
		public void setVideo(String video) {
			this.video = video;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public Integer getWidth() {
			return width;
		}
		public void setWidth(Integer width) {
			this.width = width;
		}
		public Integer getHeight() {
			return height;
		}
		public void setHeight(Integer height) {
			this.height = height;
		}
		public BigInteger getLikes() {
			return likes;
		}
		public void setLikes(BigInteger likes) {
			this.likes = likes;
		}
		public BigInteger getComments() {
			return comments;
		}
		public void setComments(BigInteger comments) {
			this.comments = comments;
		}
		
	}
	private static final long serialVersionUID = 1L;
	private String username;
	private String name;
	private BigInteger followers;
	private BigInteger totalPosts;
	private BigInteger averageLikes;
	private BigInteger averageComments;
	private List<Post> posts;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigInteger getFollowers() {
		return followers;
	}
	public void setFollowers(BigInteger followers) {
		this.followers = followers;
	}
	public BigInteger getTotalPosts() {
		return totalPosts;
	}
	public void setTotalPosts(BigInteger totalPosts) {
		this.totalPosts = totalPosts;
	}
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
	public List<Post> getPosts() {
		return posts;
	}
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
