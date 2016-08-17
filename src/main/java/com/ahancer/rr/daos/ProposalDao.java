package com.ahancer.rr.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.models.Proposal;

public interface ProposalDao extends CrudRepository<Proposal, Long> {

	public Page<Proposal> findAll(Pageable pageable);

	@Query("SELECT COUNT(cp) FROM proposal cp WHERE cp.influencer.influencerId=:influencerId AND cp.campaign.campaignId=:campaignId")
	public int countByInfluencerAndCampaign(@Param("influencerId") Long influencerId, @Param("campaignId") Long campaignId);
}
