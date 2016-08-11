package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.MediaLink;

public interface MediaLinkDao extends CrudRepository<MediaLink, Long>{
	Long countByMediaIdAndSocialId(String mediaId, String socialId);
}
