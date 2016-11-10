package com.ahancer.rr.daos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.Influencer;

@Repository
public interface InfluencerDao extends CrudRepository<Influencer, Long> {
	
	@Query("SELECT i.commission "
			+ "FROM influencer i "
			+ "WHERE i.influencerId = :influencerId")
	public Double findCommissionByInfluencerId(@Param("influencerId") Long influencerId);
	
}
