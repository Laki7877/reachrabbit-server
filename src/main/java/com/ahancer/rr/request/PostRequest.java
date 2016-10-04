package com.ahancer.rr.request;

import java.io.Serializable;

import com.ahancer.rr.models.Media;

public class PostRequest implements Serializable {

	private static final long serialVersionUID = 8046715432462180328L;
	private Media media;
	private String url;
	
	public PostRequest(){
		
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
