package com.ahancer.rr.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.custom.type.CampaignStatus;
import com.ahancer.rr.models.Campaign;

public interface CampaignDao extends CrudRepository<Campaign, Long> {
	public Page<Campaign> findByStatusNotInAndMediaMediaIdIn(Collection<CampaignStatus> statuses, Collection<String> mediaIds, Pageable pageable);
	public Page<Campaign> findByStatusNotIn(Collection<CampaignStatus> statuses, Pageable pageable);
	public Page<Campaign> findByBrandId(Long brandId, Pageable pageable);	
	public Page<Campaign> findAll(Pageable pageable);
	public List<Campaign> findByBrandBrandIdAndStatusIn(Long brandId, Collection<CampaignStatus> statuses);
	public Campaign findByCampaignIdAndBrandId(Long campaignId, Long brandId);
	
	@Query("UPDATE campaign c SET c.rabbitFlag=:rabbitFlag WHERE c.campaignId=:campaignId AND c.brandId=:brandId")
	public int updateRabbitFlag(@Param("rabbitFlag") Boolean rabbitFlag, @Param("campaignId") Long campaignId,@Param("brandId") Long brandId);

}