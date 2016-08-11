package com.ahancer.rr.response;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.ahancer.rr.models.Media;

public class OAuthenticationResponse extends AuthenticationResponse implements Serializable {
	
	private static final long serialVersionUID = -8190324180657188609L;
	private Media media;
	private String name;
	private String email;
	private String id;
	private List<Page> pages;
	private String profilePicture;
	
	public static class Page implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 4297074126284943105L;
		private String id;
		private BigInteger count;
		
		public Page(String id, BigInteger count) {
			super();
			this.id = id;
			this.count = count;
		}
		public BigInteger getCount() {
			return count;
		}

		public void setCount(BigInteger count) {
			this.count = count;
		}

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	}
	
	
	public OAuthenticationResponse() {
		super(null);
		// TODO Auto-generated constructor stub
	}
	public OAuthenticationResponse(String token) {
		super(token);
		// TODO Auto-generated constructor stub
	}
	public Media getMedia() {
		return media;
	}
	public void setMedia(Media media) {
		this.media = media;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Page> getPages() {
		return pages;
	}
	public void setPages(List<Page> pages) {
		this.pages = pages;
	}
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	
}
