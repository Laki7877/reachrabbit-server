package com.ahancer.rr.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.CampaignProposal;

public interface CampaignProposalDao extends CrudRepository<CampaignProposal, Long> {
	
	Page<CampaignProposal> findAll(Pageable pageable);
}
