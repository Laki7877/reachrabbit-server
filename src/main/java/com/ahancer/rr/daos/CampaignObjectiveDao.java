package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.CampaignObjective;

public interface CampaignObjectiveDao extends CrudRepository<CampaignObjective, Long> {
	public List<CampaignObjective> findAllByIsActiveTrueOrderByObjectiveId();
}
