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
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.response.CampaignResponse;

public interface CampaignDao extends CrudRepository<Campaign, Long> {
	
	@Query("SELECT new com.ahancer.rr.response.CampaignResponse(c, 'Influencer') FROM campaign c JOIN c.media cm WHERE c.status IN :statuses AND cm.mediaId IN :mediaIds")
	public Page<CampaignResponse> findByStatusAndMedia(@Param("statuses") Collection<CampaignStatus> statuses,@Param("mediaIds") Collection<String> mediaIds, Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.CampaignResponse(c, 'Influencer') FROM campaign c WHERE c.status IN :statuses ")
	public Page<CampaignResponse> findByStatus(@Param("statuses") Collection<CampaignStatus> statuses, Pageable pageable);
	
	
	@Query("SELECT new com.ahancer.rr.response.CampaignResponse(c, 'Brand') FROM campaign c WHERE c.brandId=:brandId")
	public Page<CampaignResponse> findByBrandId(@Param("brandId") Long brandId, Pageable pageable);	
	
	@Query("SELECT new com.ahancer.rr.response.CampaignResponse(c, 'Brand') FROM campaign c WHERE c.brandId=:brandId AND c.status=:status ")
	public Page<CampaignResponse> findByBrandIdAndStatus(@Param("brandId") Long brandId, @Param("status") CampaignStatus status, Pageable pageable);
	
	
	@Query("SELECT new com.ahancer.rr.response.CampaignResponse(c, 'Brand') FROM campaign c WHERE c.brandId=:brandId AND c.status=:status AND c.proposalDeadline > :proposalDeadline")
	public Page<CampaignResponse> findByBrandIdAndStatusOpen(@Param("brandId") Long brandId, @Param("status") CampaignStatus status, @Param("proposalDeadline") Date proposalDeadline, Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.CampaignResponse(c, 'Brand') FROM campaign c WHERE c.brandId=:brandId AND c.status=:status AND c.proposalDeadline <= :proposalDeadline")
	public Page<CampaignResponse> findByBrandIdAndStatusClose(@Param("brandId") Long brandId, @Param("status") CampaignStatus status, @Param("proposalDeadline") Date proposalDeadline, Pageable pageable);
	
	public Page<Campaign> findAll(Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.CampaignResponse(c, 'Brand') FROM campaign c WHERE c.brandId=:brandId AND c.status IN :statuses ")
	public List<CampaignResponse> findByBrandIdAndStatus(@Param("brandId") Long brandId, @Param("statuses") Collection<CampaignStatus> statuses);
	
	
	public Campaign findByCampaignIdAndBrandId(Long campaignId, Long brandId);
	
	public Campaign findByCampaignIdAndStatus(Long campaignId, CampaignStatus status);
	
	@Modifying
	@Query("UPDATE campaign c SET c.rabbitFlag=:rabbitFlag WHERE c.campaignId=:campaignId AND c.brandId=:brandId")
	public int updateRabbitFlag(@Param("rabbitFlag") Boolean rabbitFlag, @Param("campaignId") Long campaignId,@Param("brandId") Long brandId);

	
	@Query("SELECT new com.ahancer.rr.response.CampaignResponse(c, 'Admin') FROM campaign c")
	public Page<CampaignResponse> findCampaignByAdmin(Pageable pageable);
	
}