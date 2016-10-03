package com.ahancer.rr.daos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.models.Post;
import com.ahancer.rr.response.UpdatePostResponse;

public interface PostDao extends CrudRepository<Post, Long> {
	
	@Query("SELECT new com.ahancer.rr.response.UpdatePostResponse(p.socialPostId, p.mediaId, p.proposalId) "
			+ "FROM post p "
			+ "GROUP BY p.socialPostId, p.mediaId, p.proposalId "
			+ "HAVING MIN(createdAt) > :minDate")
	public List<UpdatePostResponse> findUpdatePost(@Param("minDate") Date minDate);
	

}
