package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Media;

public interface MediaDao extends CrudRepository<Media, String> {
	Media findByMediaId(String mediaId);
}
