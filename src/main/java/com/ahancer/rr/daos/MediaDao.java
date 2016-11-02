package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.Media;

@Repository
public interface MediaDao extends CrudRepository<Media, String> {
	public Media findByMediaId(String mediaId);
	public List<Media> findAllByIsActiveTrueOrderByMediaId();
}
