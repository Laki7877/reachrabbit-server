package com.ahancer.rr.daos;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.response.MessageCountResponse;

public interface ProposalMessageDao extends CrudRepository<ProposalMessage, Long> {
	
	public Long countByProposalCampaignBrandIdAndIsBrandReadFalseAndProposalStatus(Long brandId,ProposalStatus status);
	public Long countByProposalCampaignBrandIdAndIsBrandReadFalse(Long brandId);
	public Long countByProposalInfluencerIdAndIsInfluencerReadFalseAndProposalStatus(Long influencerId,ProposalStatus status);
	public Long countByProposalInfluencerIdAndIsInfluencerReadFalse(Long influencerId);
	public Long countByProposalProposalIdAndProposalCampaignBrandIdAndIsBrandReadFalse(Long proposalId, Long brandId);
	public Long countByProposalProposalIdAndProposalInfluencerIdAndIsInfluencerReadFalse(Long proposalId, Long influencerId);
	public Page<ProposalMessage> findByProposalProposalIdAndProposalCampaignBrandId(Long proposalId, Long brandId, Pageable pageable);
	public Page<ProposalMessage> findByProposalProposalIdAndProposalInfluencerId(Long proposalId, Long influencerId, Pageable pageable);
	public Page<ProposalMessage> findByProposalProposalIdAndProposalInfluencerIdAndCreatedAtBefore(Long proposalId, Long influencerId, Date createdAtBefore, Pageable pageable);
	public Page<ProposalMessage> findByProposalProposalIdAndProposalCampaignBrandIdAndCreatedAtBefore(Long proposalId, Long brandId, Date createdAtBefore, Pageable pageable);
	public List<ProposalMessage> findByProposalProposalIdAndCreatedAtAfterOrderByCreatedAtDesc(Long proposalId, Date createdAtAfter);
	public Long countByProposalProposalIdAndCreatedAtAfterOrderByCreatedAtDesc(Long proposalId, Date createdAtAfter);

	
	
	@Query("SELECT new com.ahancer.rr.response.MessageCountResponse(pm.proposal.campaign.brand.user.email, COUNT(pm)) "
			+ "FROM proposalMessage pm "
			+ "WHERE pm.isBrandRead=:isBrandRead AND pm.createdAt>=:from AND pm.createdAt<=:to "
			+ "GROUP BY pm.proposal.campaign.brand.user.email "
			+ "HAVING COUNT(pm) > 0")
	public List<MessageCountResponse> findBrandMessageCount(@Param("isBrandRead") Boolean isBrandRead,@Param("from") Date from,@Param("to") Date to);
	
	@Query("SELECT new com.ahancer.rr.response.MessageCountResponse(pm.proposal.influencer.user.email, COUNT(pm)) "
			+ "FROM proposalMessage pm "
			+ "WHERE pm.isInfluencerRead=:isInfluencerRead AND pm.createdAt>=:from AND pm.createdAt<=:to "
			+ "GROUP BY pm.proposal.influencer.user.email "
			+ "HAVING COUNT(pm) > 0")
	public List<MessageCountResponse> findInfluencerMessageCount(@Param("isInfluencerRead") Boolean isInfluencerRead,@Param("from") Date from,@Param("to") Date to);
	
}
