package com.ahancer.rr.daos;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Influencer;

@Transactional
public interface InfluencerDao extends CrudRepository<Influencer, Long> {

}
