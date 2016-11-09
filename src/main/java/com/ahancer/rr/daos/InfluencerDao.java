package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.Influencer;

@Repository
public interface InfluencerDao extends CrudRepository<Influencer, Long> {
	
	public Double findCommissionByInfluencerId(Long influencerId);
	
}
