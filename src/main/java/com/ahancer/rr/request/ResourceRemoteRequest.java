package com.ahancer.rr.request;

import java.io.Serializable;
import java.net.URL;

public class ResourceRemoteRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6458453346810719139L;
	private URL url;
	
	public ResourceRemoteRequest(URL url) {
		super();
		this.url = url;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
}
