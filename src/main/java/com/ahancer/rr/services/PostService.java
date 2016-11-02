package com.ahancer.rr.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ahancer.rr.models.Post;
import com.ahancer.rr.request.PostRequest;
import com.ahancer.rr.response.UpdatePostResponse;

@Service
public interface PostService {
	public List<UpdatePostResponse> getUpdatePost(Date minDate) throws Exception;
	public Post createNewPostBySys(Post post) throws Exception;
	public Post createPostByAdmin(Long proposalId, PostRequest request) throws Exception;
	public void createPostSchedule(Date dataDate) throws Exception;
	public List<UpdatePostResponse> getListPost(Long proposalId) throws Exception;
	public int deletePost(Long proposalId, PostRequest request) throws Exception;

}
