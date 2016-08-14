package com.ahancer.rr.request;

import java.io.Serializable;

public class ResourceRemoteRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6458453346810719139L;
	private String url;
	
	public ResourceRemoteRequest(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
