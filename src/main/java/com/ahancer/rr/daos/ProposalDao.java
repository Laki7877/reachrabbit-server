package com.ahancer.rr.daos;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.models.Proposal;

public interface ProposalDao extends CrudRepository<Proposal, Long> {
	
	public Proposal findByProposalIdAndInfluencerId(Long proposalId,Long influencerId);
	
	public Proposal findByProposalIdAndCampaignBrandId(Long proposalId,Long brandId);

	public Page<Proposal> findByInfluencerId(Long influencerId,Pageable pageable);
	
	public Page<Proposal> findByCampaignBrandId(Long brandId,Pageable pageable);
	
	public Page<Proposal> findAll(Pageable pageable);

	@Query("SELECT COUNT(cp) FROM proposal cp WHERE cp.influencer.influencerId=:influencerId AND cp.campaign.campaignId=:campaignId")
	public int countByInfluencerAndCampaign(@Param("influencerId") Long influencerId, @Param("campaignId") Long campaignId);
	
	@Modifying
	@Query("UPDATE proposal cp SET messageUpdatedAt=:messageUpdatedAt WHERE cp.proposalId=:proposalId AND ( "
			+ " ( cp.influencerId=:userId ) OR ( cp.campaign.brandId=:userId ) "
			+ " )")
	public int updateMessageUpdatedAt(@Param("proposalId") Long proposalId,  @Param("influencerId") Long influencerId,@Param ("brandId") Long brandId, @Param("messageUpdatedAt") Date messageUpdatedAt);
	
	
}
