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

import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.response.MessageCountResponse;
import com.ahancer.rr.response.ProposalMessageResponse;

public interface ProposalMessageDao extends CrudRepository<ProposalMessage, Long> {
	
	public Long countByProposalCampaignBrandIdAndIsBrandReadFalseAndProposalStatus(Long brandId,ProposalStatus status);
	public Long countByProposalCampaignBrandIdAndIsBrandReadFalse(Long brandId);
	public Long countByProposalInfluencerIdAndIsInfluencerReadFalseAndProposalStatus(Long influencerId,ProposalStatus status);
	public Long countByProposalInfluencerIdAndIsInfluencerReadFalse(Long influencerId);
	public Long countByProposalProposalIdAndProposalCampaignBrandIdAndIsBrandReadFalse(Long proposalId, Long brandId);
	public Long countByProposalProposalIdAndProposalInfluencerIdAndIsInfluencerReadFalse(Long proposalId, Long influencerId);
	public Long countByProposalProposalIdAndCreatedAtAfterOrderByCreatedAtDesc(Long proposalId, Date createdAtAfter);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalMessageResponse(pm,'Brand') "
			+ "FROM proposalMessage pm "
			+ "WHERE pm.proposalId=:proposalId AND pm.proposal.campaign.brandId=:brandId")
	public Page<ProposalMessageResponse> findByProposalProposalIdAndProposalCampaignBrandId(@Param("proposalId") Long proposalId, @Param("brandId") Long brandId, Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalMessageResponse(pm,'Influencer') "
			+ "FROM proposalMessage pm "
			+ "WHERE pm.proposalId=:proposalId AND pm.proposal.influencerId=:influencerId")
	public Page<ProposalMessageResponse> findByProposalProposalIdAndProposalInfluencerId(@Param("proposalId") Long proposalId,@Param("influencerId") Long influencerId, Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalMessageResponse(pm,'Influencer') "
			+ "FROM proposalMessage pm "
			+ "WHERE pm.proposalId=:proposalId AND pm.createdAt < :createdAtBefore AND pm.proposal.influencerId=:influencerId")
	public Page<ProposalMessageResponse> findByProposalProposalIdAndProposalInfluencerIdAndCreatedAtBefore(@Param("proposalId") Long proposalId,@Param("influencerId") Long influencerId,@Param("createdAtBefore") Date createdAtBefore, Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalMessageResponse(pm,'Brand') "
			+ "FROM proposalMessage pm "
			+ "WHERE pm.proposalId=:proposalId AND pm.createdAt < :createdAtBefore AND pm.proposal.campaign.brandId=:brandId ")
	public Page<ProposalMessageResponse> findByProposalProposalIdAndProposalCampaignBrandIdAndCreatedAtBefore(@Param("proposalId") Long proposalId,@Param("brandId") Long brandId,@Param("createdAtBefore") Date createdAtBefore, Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalMessageResponse(pm,'Brand') "
			+ "FROM proposalMessage pm "
			+ "WHERE pm.proposalId=:proposalId AND pm.createdAt > :createdAtBefore "
			+ "ORDER BY pm.createdAt DESC")
	public List<ProposalMessageResponse> findByProposalProposalIdAndCreatedAtAfterOrderByCreatedAtDesc(@Param("proposalId") Long proposalId, @Param("createdAtBefore") Date createdAtAfter);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalMessageResponse(pm,'Brand') "
			+ "FROM proposalMessage pm "
			+ "WHERE pm.proposalId=:proposalId AND pm.createdAt > :createdAtBefore AND pm.proposal.campaign.brandId=:brandId "
			+ "ORDER BY pm.createdAt DESC")
	public List<ProposalMessageResponse> findByProposalProposalIdAndProposalCampaignBrandIdAndCreatedAtAfterOrderByCreatedAtDesc(@Param("proposalId") Long proposalId,@Param("brandId") Long brandId, @Param("createdAtBefore") Date createdAtAfter);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalMessageResponse(pm,'Influencer') "
			+ "FROM proposalMessage pm "
			+ "WHERE pm.proposalId=:proposalId AND pm.createdAt > :createdAtBefore AND pm.proposal.influencerId=:influencerId "
			+ "ORDER BY pm.createdAt DESC")
	public List<ProposalMessageResponse> findByProposalProposalIdAndProposalInfluencerIdAndCreatedAtAfterOrderByCreatedAtDesc(@Param("proposalId") Long proposalId,@Param("influencerId") Long influencerId, @Param("createdAtBefore") Date createdAtAfter);

	
	@Modifying
	@Query("UPDATE proposalMessage pm SET pm.isBrandRead=:isBrandRead "
			+ "WHERE pm.proposalId=:proposalId AND pm.messageId IN :messageIds ")
	public int updateBrandReadMessage(@Param("proposalId") Long proposalId,@Param("messageIds") Collection<Long> messageIds, @Param("isBrandRead") Boolean isBrandRead);
	
	@Modifying
	@Query("UPDATE proposalMessage pm SET pm.isInfluencerRead=:isInfluencerRead "
			+ "WHERE pm.proposalId=:proposalId AND pm.messageId IN :messageIds ")
	public int updateInfluencerReadMessage(@Param("proposalId") Long proposalId, @Param("messageIds") Collection<Long> messageIds, @Param("isInfluencerRead") Boolean isInfluencerRead);
	
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
