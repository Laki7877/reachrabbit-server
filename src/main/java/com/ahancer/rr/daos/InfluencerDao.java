package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Influencer;


public interface InfluencerDao extends CrudRepository<Influencer, Long> {
	
}
