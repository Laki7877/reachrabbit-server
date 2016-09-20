package com.ahancer.rr.response;

import java.util.List;

import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.Video;

public class YouTubeProfileResponse {
	private List<Video> videos;
	private Channel channel;
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}
}
