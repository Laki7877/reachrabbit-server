package com.ahancer.rr.daos;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.custom.type.CampaignStatus;
import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.models.Media;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.response.ProposalDashboardResponse;
import com.ahancer.rr.response.ProposalResponse;

@Repository
public interface ProposalDao extends CrudRepository<Proposal, Long> {
	
	public Proposal findByProposalIdAndInfluencerId(Long proposalId,Long influencerId);
	public Proposal findByProposalIdAndCampaignBrandId(Long proposalId,Long brandId);
	public Proposal findByInfluencerIdAndCampaignCampaignId(Long influencerId, Long campaignId);
	
	public List<Proposal> findByInfluencerIdAndCampaignStatusIn(Long influencerId, Collection<CampaignStatus> statuses);
	public Page<Proposal> findByInfluencerId(Long influencerId,Pageable pageable);
	public Page<Proposal> findByInfluencerIdAndCampaignCampaignId(Long influencerId, Long campaignId, Pageable pageable);
	public Page<Proposal> findByInfluencerIdAndCampaignCampaignIdAndMessageUpdatedAtAfter(Long influencerId, Long campaignId, Date date, Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalResponse(p, 'Influencer') "
			+ "FROM proposal p "
			+ "WHERE p.influencerId = :influencerId "
			+ "AND p.status = :status "
			+ "AND p.messageUpdatedAt > :date ")
	public Page<ProposalResponse> findByInfluencerIdAndStatusAndMessageUpdatedAtAfter(@Param("influencerId") Long influencerId,@Param("status") ProposalStatus status,@Param("date") Date date, Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalResponse(p, 'Influencer') "
			+ "FROM proposal p "
			+ "WHERE p.influencerId = :influencerId "
			+ "AND p.status = :status "
			+ "AND p.campaign.title like CONCAT('%', :search , '%') "
			+ "AND p.messageUpdatedAt > :date ")
	public Page<ProposalResponse> findByInfluencerIdAndStatusAndCampaignTitleContainingAndMessageUpdatedAtAfter(@Param("influencerId") Long influencerId,@Param("status") ProposalStatus status,@Param("search") String search,@Param("date") Date date, Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalResponse(p, 'Influencer') "
			+ "FROM proposal p "
			+ "WHERE p.influencerId = :influencerId ")
	public List<ProposalResponse> findByInfluencerId(Long influencerId);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalResponse(p, 'Admin') "
			+ "FROM proposal p "
			+ "WHERE p.status = :status ")
	public Page<ProposalResponse> findByStatus(@Param("status") ProposalStatus status, Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalResponse(p, 'Admin') "
			+ "FROM proposal p "
			+ "WHERE p.status = :status "
			+ "AND p.campaign.title like CONCAT('%', :search , '%') " )
	public Page<ProposalResponse> findByStatusAndCampaignTitleContaining(@Param("status") ProposalStatus status,@Param("search") String search, Pageable pageable);
	
	public Page<Proposal> findByCampaignBrandId(Long brandId,Pageable pageable);
	
	
	
	@Query("SELECT new com.ahancer.rr.response.ProposalResponse(p, 'Brand') "
			+ "FROM proposal p "
			+ "WHERE p.campaign.brandId = :brandId "
			+ "AND p.status = :status "
			+ "AND p.messageUpdatedAt > :date ")
	public Page<ProposalResponse> findByCampaignBrandIdAndStatusAndMessageUpdatedAtAfter(@Param("brandId") Long brandId,@Param("status") ProposalStatus status, @Param("date") Date date, Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalResponse(p, 'Brand') "
			+ "FROM proposal p "
			+ "WHERE p.campaign.brandId = :brandId "
			+ "AND p.status = :status "
			+ "AND p.campaign.title like CONCAT('%', :search , '%') "
			+ "AND p.messageUpdatedAt > :date ")
	public Page<ProposalResponse> findByCampaignBrandIdAndStatusAndCampaignTitleContainingAndMessageUpdatedAtAfter(@Param("brandId") Long brandId,@Param("status") ProposalStatus status,@Param("search") String search, @Param("date") Date date, Pageable pageable);
	
	public Page<Proposal> findByCampaignBrandIdAndCampaignCampaignId(Long brandId, Long campaignId, Pageable pageable);
	public Page<Proposal> findByCampaignBrandIdAndCampaignCampaignIdAndMessageUpdatedAtAfter(Long brandId, Long campaignId, Date date, Pageable pageable);
	public List<Proposal> findByCampaignBrandIdAndCampaignCampaignId(Long brandId, Long campaignId);
	
	
	public Page<Proposal> findAll(Pageable pageable);

	public Long countByInfluencerInfluencerIdAndCampaignCampaignId(Long influencerId, Long campaignId);
	
	public Long countByProposalIdAndCampaignBrandId(Long proposalId,Long brandId);
	
	public Long countByInfluencerInfluencerIdAndStatus(Long influencerId, ProposalStatus status);
	
	public Long countByStatus(ProposalStatus status);
	
	
	@Query("SELECT p "
			+ "FROM proposal p "
			+ "WHERE p.campaign.brand.user.referral.referralId IS NOT NULL "
			+ "AND p.status in :statuses ")
	public Page<Proposal> findAllByCampaignBrandUserReferralReferralIdNotNull(@Param("statuses") Collection<ProposalStatus> statuses, Pageable pageable);
	
	@Query("SELECT p "
			+ "FROM proposal p "
			+ "WHERE p.campaign.brand.user.referral.referralId IS NOT NULL "
			+ "AND p.status in :statuses "
			+ "AND (p.campaign.brand.user.referral.referralId LIKE CONCAT('%', :search , '%') "
			+ "OR p.campaign.title LIKE CONCAT('%', :search , '%') "
			+ "OR p.campaign.brand.brandName LIKE CONCAT('%', :search , '%') "
			+ "OR p.campaign.brand.user.referral.partner.email LIKE CONCAT('%', :search , '%') )")
	public Page<Proposal> findAllByCampaignBrandUserReferralReferralIdNotNullAndSearch(@Param("statuses") Collection<ProposalStatus> statuses,@Param("search") String search, Pageable pageable);
	
	
	public Long countByCampaignBrandIdAndStatus(Long brandId, ProposalStatus status);
	public Long countByCampaignBrandIdAndInfluencerId(Long brandId, Long influencerId);
	
	public Long countByCampaignBrandUserReferralReferralIdAndStatusIn(String referralId,Collection<ProposalStatus> statuses);
	
	public Long countByInfluencerIdAndMediaMediaId(Long influencerId, String mediaId);
	
	@Modifying
	@Query("UPDATE proposal cp SET messageUpdatedAt=:messageUpdatedAt "
			+ "WHERE cp.proposalId=:proposalId "
			+ "AND cp.influencerId=:influencerId")
	public int updateMessageUpdatedAtByInfluencer(@Param("proposalId") Long proposalId,  @Param("influencerId") Long influencerId, @Param("messageUpdatedAt") Date messageUpdatedAt);
	
	@Modifying
	@Query("UPDATE proposal cp "
			+ "SET messageUpdatedAt=:messageUpdatedAt "
			+ "WHERE cp.proposalId=:proposalId")
	public int updateMessageUpdatedAtByProposal(@Param("proposalId") Long proposalId, @Param("messageUpdatedAt") Date messageUpdatedAt);
	
	@Modifying
	@Query("UPDATE proposal cp "
			+ "SET status=:status "
			+ "WHERE cp.proposalId=:proposalId")
	public int updateProposalStatus(@Param("proposalId") Long proposalId, @Param("status") ProposalStatus status);
	
	
	@Modifying
	@Query("UPDATE proposal p "
			+ "SET p.rabbitFlag=:rabbitFlag "
			+ "WHERE p.proposalId=:proposalId "
			+ "AND p.influencerId=:influencerId")
	public int updateRabbitFlag(@Param("rabbitFlag") Boolean rabbitFlag, @Param("proposalId") Long proposalId,@Param("influencerId") Long influencerId);

	@Modifying
	@Query("UPDATE proposal p "
			+ "SET p.hasPost = :hasPost "
			+ "WHERE p.proposalId = :proposalId ")
	public int updateHasPost(@Param("hasPost") Boolean hasPost, @Param("proposalId") Long proposalId);
	
	@Modifying
	@Query("UPDATE proposal p "
			+ "SET p.isReferralPay = :isReferralPay "
			+ "WHERE p.proposalId = :proposalId ")
	public int updateIsReferralPay(@Param("isReferralPay") Boolean isReferralPay, @Param("proposalId") Long proposalId);
	
	
	@Query("SELECT new com.ahancer.rr.response.ProposalDashboardResponse(p.proposalId, p.influencerId, p.influencer, p.price, p.status) "
			+ "FROM proposal p "
			+ "WHERE p.campaign.campaignId = :campaignId "
			+ "AND p.status in :statuses ")
	public List<ProposalDashboardResponse> getListProposalByCampaignAndStatus(@Param("campaignId") Long campaignId, @Param("statuses") Collection<ProposalStatus> statuses);
	
	@Query("SELECT new com.ahancer.rr.response.ProposalDashboardResponse(p.proposalId, p.influencerId, p.influencer, p.price, p.status) "
			+ "FROM proposal p "
			+ "WHERE p.campaign.campaignId = :campaignId "
			+ "AND p.campaign.brandId = :brandId "
			+ "AND p.status in :statuses ")
	public List<ProposalDashboardResponse> getListProposalByCampaignAndBrandAndStatus(@Param("campaignId") Long campaignId, @Param("brandId") Long brandId, @Param("statuses") Collection<ProposalStatus> statuses);

	@Query("SELECT m "
			+ "FROM proposal p "
			+ "LEFT OUTER JOIN p.media m "
			+ "WHERE p.proposalId = :proposalId ")
	public Set<Media> getMediaFromProposal(@Param("proposalId") Long proposalId);
	
	
	
}
