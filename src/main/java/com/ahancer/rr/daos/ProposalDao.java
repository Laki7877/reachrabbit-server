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
	public Proposal findByInfluencerIdAndCampaignCampaignId(Long influencerId, Long campaignId);

	
	public List<Proposal> findByInfluencerIdAndCampaignStatusIn(Long influencerId, Collection<CampaignStatus> statuses);
	public Page<Proposal> findByInfluencerId(Long influencerId,Pageable pageable);
	public Page<Proposal> findByInfluencerIdAndCampaignCampaignId(Long influencerId, Long campaignId, Pageable pageable);
	public Page<Proposal> findByInfluencerIdAndCampaignCampaignIdAndMessageUpdatedAtAfter(Long influencerId, Long campaignId, Date date, Pageable pageable);
	public Page<Proposal> findByInfluencerIdAndStatusAndMessageUpdatedAtAfter(Long influencerId,ProposalStatus status, Date date, Pageable pageable);
	public Page<Proposal> findByInfluencerIdAndStatusAndCampaignTitleContainingAndMessageUpdatedAtAfter(Long influencerId,ProposalStatus status, String search, Date date, Pageable pageable);
	
	public List<Proposal> findByInfluencerId(Long influencerId);
	
	public Page<Proposal> findByStatus(ProposalStatus status, Pageable pageable);
	public Page<Proposal> findByStatusAndCampaignTitleContaining(ProposalStatus status, String search, Pageable pageable);
	
	public Page<Proposal> findByCampaignBrandId(Long brandId,Pageable pageable);
	public Page<Proposal> findByCampaignBrandIdAndStatusAndMessageUpdatedAtAfter(Long brandId,ProposalStatus status, Date date, Pageable pageable);
	
	public Page<Proposal> findByCampaignBrandIdAndStatusAndCampaignTitleContainingAndMessageUpdatedAtAfter(Long brandId,ProposalStatus status, String search, Date date, Pageable pageable);
	
	public Page<Proposal> findByCampaignBrandIdAndCampaignCampaignId(Long brandId, Long campaignId, Pageable pageable);
	public Page<Proposal> findByCampaignBrandIdAndCampaignCampaignIdAndMessageUpdatedAtAfter(Long brandId, Long campaignId, Date date, Pageable pageable);
	public List<Proposal> findByCampaignBrandIdAndCampaignCampaignId(Long brandId, Long campaignId);
	
	
	public Page<Proposal> findAll(Pageable pageable);

	public Long countByInfluencerInfluencerIdAndCampaignCampaignId(Long influencerId, Long campaignId);
	public Long countByProposalIdAndCampaignBrandId(@Param("proposalId") Long proposalId,@Param("brandId") Long brandId);
	public Long countByInfluencerInfluencerIdAndStatus(Long influencerId, ProposalStatus status);
	
	public Long countByStatus(ProposalStatus status);
	
	
	public Long countByCampaignBrandIdAndStatus(Long brandId, ProposalStatus status);
	public Long countByCampaignBrandIdAndInfluencerId(Long brandId, Long influencerId);
	
	public Long countByInfluencerIdAndMediaMediaId(Long influencerId, String mediaId);
	
	@Modifying
	@Query("UPDATE proposal cp SET messageUpdatedAt=:messageUpdatedAt WHERE cp.proposalId=:proposalId AND cp.influencerId=:influencerId")
	public int updateMessageUpdatedAtByInfluencer(@Param("proposalId") Long proposalId,  @Param("influencerId") Long influencerId, @Param("messageUpdatedAt") Date messageUpdatedAt);
	
	@Modifying
	@Query("UPDATE proposal cp SET messageUpdatedAt=:messageUpdatedAt WHERE cp.proposalId=:proposalId")
	public int updateMessageUpdatedAtByProposal(@Param("proposalId") Long proposalId, @Param("messageUpdatedAt") Date messageUpdatedAt);
	
	@Modifying
	@Query("UPDATE proposal cp SET status=:status WHERE cp.proposalId=:proposalId")
	public int updateProposalStatus(@Param("proposalId") Long proposalId, @Param("status") ProposalStatus status);
	
	
	@Modifying
	@Query("UPDATE proposal p SET p.rabbitFlag=:rabbitFlag WHERE p.proposalId=:proposalId AND p.influencerId=:influencerId")
	public int updateRabbitFlag(@Param("rabbitFlag") Boolean rabbitFlag, @Param("proposalId") Long proposalId,@Param("influencerId") Long influencerId);

	@Modifying
	@Query("UPDATE proposal p SET p.hasPost = :hasPost WHERE p.proposalId = :proposalId ")
	public int updateHasPost(@Param("hasPost") Boolean hasPost, @Param("proposalId") Long proposalId);

}
