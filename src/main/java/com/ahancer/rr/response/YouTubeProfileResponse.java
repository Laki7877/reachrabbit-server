package com.ahancer.rr.response;

import java.util.List;

import com.google.api.services.youtube.model.PlaylistItem;

public class YouTubeProfileResponse {
	private List<PlaylistItem> posts;

	public List<PlaylistItem> getPosts() {
		return posts;
	}

	public void setPosts(List<PlaylistItem> posts) {
		this.posts = posts;
	}
}
