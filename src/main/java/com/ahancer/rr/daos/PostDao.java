package com.ahancer.rr.daos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.Post;
import com.ahancer.rr.response.PostAggregateResponse;
import com.ahancer.rr.response.UpdatePostResponse;

@Repository
public interface PostDao extends CrudRepository<Post, Long> {
	
	@Query("SELECT new com.ahancer.rr.response.UpdatePostResponse(p.socialPostId, p.mediaId, p.proposalId, p.url, p.isPersonalAccountPost) "
			+ "FROM post p "
			+ "GROUP BY p.socialPostId, p.mediaId, p.proposalId, p.url, p.isPersonalAccountPost "
			+ "HAVING MIN(dataDate) > :minDate")
	public List<UpdatePostResponse> findUpdatePost(@Param("minDate") Date minDate);
	
	@Query("SELECT new com.ahancer.rr.response.UpdatePostResponse(p.socialPostId, p.mediaId, p.proposalId, p.url, p.media, p.isPersonalAccountPost) "
			+ "FROM post p "
			+ "WHERE p.proposalId = :proposalId "
			+ "GROUP BY p.socialPostId, p.mediaId, p.proposalId, p.url, p.isPersonalAccountPost ")
	public List<UpdatePostResponse> findPostByProposalId(@Param("proposalId") Long proposalId);
	
	@Modifying
	@Query("DELETE FROM post p "
			+ "WHERE p.proposalId = :proposalId "
			+ "AND p.mediaId = :mediaId "
			+ "AND p.socialPostId = :socialPostId ")
	public int deletePost(@Param("proposalId") Long proposalId, @Param("mediaId") String mediaId, @Param("socialPostId") String socialPostId );
	
	public Long countByProposalId(Long proposalId);
	
	@Query("SELECT new com.ahancer.rr.response.PostAggregateResponse(p.dataDate, p.mediaId, SUM(p.likeCount), SUM(p.commentCount), SUM(p.viewCount), SUM(p.shareCount) ) "
			+ "FROM post p "
			+ "WHERE p.proposalId = :proposalId "
			+ "GROUP BY p.dataDate ,p.mediaId")
	public List<PostAggregateResponse> getAggregatePost(@Param("proposalId") Long proposalId);
	
	
	@Query("SELECT count(*) "
			+ "FROM post p "
			+ "WHERE p.proposalId = :proposalId "
			+ "AND p.mediaId = :mediaId "
			+ "AND p.socialPostId = :socialPostId ")
	public Long countByMediaIdAndSocialPostId(@Param("proposalId") Long proposalId, @Param("mediaId") String mediaId, @Param("socialPostId") String socialPostId);
	
}
