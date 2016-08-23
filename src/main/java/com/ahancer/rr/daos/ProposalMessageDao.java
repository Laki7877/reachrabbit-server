package com.ahancer.rr.daos;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.ProposalMessage;

public interface ProposalMessageDao extends CrudRepository<ProposalMessage, Long> {
	
	public Long countByProposalCampaignBrandIdAndIsBrandReadFalse(Long brandId);
	public Long countByProposalInfluencerIdAndIsInfluencerReadFalse(Long influencerId);
	public Long countByProposalProposalIdAndProposalCampaignBrandIdAndIsBrandReadFalse(Long proposalId, Long brandId);
	public Long countByProposalProposalIdAndProposalInfluencerIdAndIsInfluencerReadFalse(Long proposalId, Long influencerId);
	public Page<ProposalMessage> findByProposalProposalIdAndProposalCampaignBrandId(Long proposalId, Long brandId, Pageable pageable);
	public Page<ProposalMessage> findByProposalProposalIdAndProposalInfluencerId(Long proposalId, Long influencerId, Pageable pageable);
	public Page<ProposalMessage> findByProposalProposalIdAndProposalInfluencerIdAndCreatedAtBefore(Long proposalId, Long influencerId, Date createdAtBefore, Pageable pageable);
	public Page<ProposalMessage> findByProposalProposalIdAndProposalCampaignBrandIdAndCreatedAtBefore(Long proposalId, Long brandId, Date createdAtBefore, Pageable pageable);
	public List<ProposalMessage> findByProposalProposalIdAndCreatedAtAfterOrderByCreatedAtDesc(Long proposalId, Date createdAtAfter);
}
