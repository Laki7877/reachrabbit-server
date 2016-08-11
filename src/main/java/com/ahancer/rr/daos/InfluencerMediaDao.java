package com.ahancer.rr.daos;

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
}
