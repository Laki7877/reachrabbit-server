package com.ahancer.rr.daos;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.models.InfluencerMedia;

public interface InfluencerMediaDao extends CrudRepository<InfluencerMedia, Long> {

	@Query("SELECT COUNT(im)"
			+ " FROM influencerMedia im"
			+ " WHERE im.influencerMediaId.mediaId=:mediaId"
			+ "   AND im.socialId=:socialId")
	public int countByMediaIdAndSocialId(@Param("mediaId") String mediaId,@Param("socialId") String socialId);
	
	
	@Modifying
	@Query("DELETE FROM influencerMedia im WHERE im.influencerMediaId.influencerId=:influencerId")
	public void deleteByInfluencerId(@Param("influencerId") Long influencerId);
	
	
	@Modifying
	@Query(value = "INSERT INTO influencerMedia (influencerId, mediaId, followerCount, pageId, socialId) VALUES (:influencerId, :mediaId, :followerCount, :pageId, :socialId)", nativeQuery = true)
	public void insertInfluencerMedia(
			  @Param("influencerId") Long influencerId
			, @Param("mediaId") String mediaId
			, @Param("followerCount") Long followerCount
			, @Param("pageId") String pageId
			, @Param("socialId") String socialId);
	
}
