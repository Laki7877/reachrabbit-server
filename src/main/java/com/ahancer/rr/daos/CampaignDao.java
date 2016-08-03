package com.ahancer.rr.daos;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Campaign;

@Transactional
public interface CampaignDao extends CrudRepository<Campaign, Long>{

}
