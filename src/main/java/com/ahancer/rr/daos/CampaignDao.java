package com.ahancer.rr.daos;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.models.Campaign;

@Transactional
public interface CampaignDao extends CrudRepository<Campaign, Long>{
	@Query("SELECT c from campaign c WHERE c.brandId = :brandId")
	Page<Campaign> findAllByBrand(@Param("brandId") Long brandId, Pageable pageable);
	
	@Query("SELECT c from campaign c WHERE c.brandId=:brandId AND c.campaignId=:campaignId")
	Campaign findOneWithBrandId(@Param("campaignId") Long campaignId, @Param("brandId") Long brandId);
}