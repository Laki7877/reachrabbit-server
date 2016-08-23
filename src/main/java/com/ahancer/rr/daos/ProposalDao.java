package com.ahancer.rr.daos;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.custom.type.CampaignStatus;
import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.models.Proposal;

public interface ProposalDao extends CrudRepository<Proposal, Long> {
	
	public Proposal findByProposalIdAndInfluencerId(Long proposalId,Long influencerId);
	
	public Proposal findByProposalIdAndCampaignBrandId(Long proposalId,Long brandId);

	public List<Proposal> findByInfluencerIdAndCampaignStatusIn(Long influencerId, Collection<CampaignStatus> statuses);
	public Page<Proposal> findByInfluencerId(Long influencerId,Pageable pageable);
	public Page<Proposal> findByInfluencerIdAndCampaignCampaignId(Long influencerId, Long campaignId, Pageable pageable);
	public Page<Proposal> findByInfluencerIdAndCampaignCampaignIdAndMessageUpdatedAtAfter(Long influencerId, Long campaignId, Date date, Pageable pageable);
	public Page<Proposal> findByInfluencerIdAndMessageUpdatedAtAfter(Long influencerId, Date date, Pageable pageable);
	
	public List<Proposal> findByInfluencerId(Long influencerId);
	
	public Page<Proposal> findByCampaignBrandId(Long brandId,Pageable pageable);
	public Page<Proposal> findByCampaignBrandIdAndMessageUpdatedAtAfter(Long brandId, Date date, Pageable pageable);
	public Page<Proposal> findByCampaignBrandIdAndCampaignCampaignId(Long brandId, Long campaignId, Pageable pageable);
	public Page<Proposal> findByCampaignBrandIdAndCampaignCampaignIdAndMessageUpdatedAtAfter(Long brandId, Long campaignId, Date date, Pageable pageable);
	
	public Page<Proposal> findAll(Pageable pageable);

	@Query("SELECT COUNT(cp) FROM proposal cp WHERE cp.influencer.influencerId=:influencerId AND cp.campaign.campaignId=:campaignId")
	public Long countByInfluencerAndCampaign(@Param("influencerId") Long influencerId, @Param("campaignId") Long campaignId);
	
	@Query("SELECT COUNT(cp) FROM proposal cp WHERE cp.influencer.influencerId=:influencerId AND cp.status=:status")
	public Long countByInfluencer(@Param("influencerId") Long influencerId, @Param("status") ProposalStatus status);
	
	@Query("SELECT COUNT(cp) FROM proposal cp WHERE proposalId=:proposalId AND cp.campaign.brandId=:brandId")
	public Long countByBrand(@Param("proposalId") Long proposalId,@Param("brandId") Long brandId);
	
	@Query("SELECT COUNT(cp) FROM proposal cp WHERE cp.campaign.brandId=:brandId AND cp.status=:status")
	public Long countByBrand(@Param("brandId") Long brandId,  @Param("status") ProposalStatus status);
	
	@Modifying
	@Query("UPDATE proposal cp SET messageUpdatedAt=:messageUpdatedAt WHERE cp.proposalId=:proposalId AND cp.influencerId=:influencerId")
	public int updateMessageUpdatedAtByInfluencer(@Param("proposalId") Long proposalId,  @Param("influencerId") Long influencerId, @Param("messageUpdatedAt") Date messageUpdatedAt);
	
	@Modifying
	@Query("UPDATE proposal cp SET messageUpdatedAt=:messageUpdatedAt WHERE cp.proposalId=:proposalId")
	public int updateMessageUpdatedAtByBrand(@Param("proposalId") Long proposalId, @Param("messageUpdatedAt") Date messageUpdatedAt);
	
	@Modifying
	@Query("UPDATE proposal cp SET status=:status WHERE cp.proposalId=:proposalId")
	public int updateProposalStatus(@Param("proposalId") Long proposalId, @Param("status") ProposalStatus status);
	
	
	
}
