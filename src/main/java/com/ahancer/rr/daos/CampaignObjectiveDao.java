package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.CampaignObjective;

@Repository
public interface CampaignObjectiveDao extends CrudRepository<CampaignObjective, Long> {
	public List<CampaignObjective> findAllByIsActiveTrueOrderByObjectiveId();
}
