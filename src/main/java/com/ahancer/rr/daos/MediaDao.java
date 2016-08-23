package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Media;

public interface MediaDao extends CrudRepository<Media, String> {
	public Media findByMediaId(String mediaId);
	
	public List<Media> findAllByOrderByMediaId();
}
