package com.ahancer.rr.daos;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.models.ProposalMessage;

public interface ProposalMessageDao extends CrudRepository<ProposalMessage, Long> {
	
	@Query("SELECT count(pm) FROM proposalMessage pm WHERE pm.proposal.proposalId=:proposalId AND pm.isBrandRead=FALSE AND pm.proposal.campaign.brandId=:brandId")
	public Long countByUnreadProposalIdForBrand(@Param("proposalId") Long proposalId, @Param("brandId") Long brandId);
	@Query("SELECT count(pm) FROM proposalMessage pm WHERE pm.proposal.proposalId=:proposalId AND pm.isInfluencerRead=FALSE AND pm.proposal.influencerId=:influencerId")
	public Long countByUnreadProposalIdForInfluencer(@Param("proposalId") Long proposalId, @Param("influencerId") Long influencerId);
	public Page<ProposalMessage> findByProposalProposalIdAndProposalCampaignBrandId(Long proposalId, Long brandId, Pageable pageable);
	public Page<ProposalMessage> findByProposalProposalIdAndProposalInfluencerId(Long proposalId, Long influencerId, Pageable pageable);
	public Page<ProposalMessage> findByProposalProposalIdAndProposalInfluencerIdAndCreatedAtBefore(Long proposalId, Long influencerId, Date createdAtBefore, Pageable pageable);
	public Page<ProposalMessage> findByProposalProposalIdAndProposalCampaignBrandIdAndCreatedAtBefore(Long proposalId, Long brandId, Date createdAtBefore, Pageable pageable);
	public List<ProposalMessage> findByProposalProposalIdAndCreatedAtAfterOrderByCreatedAtDesc(Long proposalId, Date createdAtAfter);
}
