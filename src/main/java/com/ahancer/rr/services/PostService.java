package com.ahancer.rr.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.PostDao;
import com.ahancer.rr.models.Post;
import com.ahancer.rr.response.UpdatePostResponse;

@Service
@Transactional(rollbackFor=Exception.class)
public class PostService {
	
	@Autowired
	private PostDao postDao;
	
	public List<UpdatePostResponse> getUpdatePost(Date minDate){
		return postDao.findUpdatePost(minDate);
	}
	
	public Post createNewPostBySys(Post post){
		return postDao.save(post);
	}
}
