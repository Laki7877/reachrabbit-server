package com.ahancer.rr.daos;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.custom.type.CampaignStatus;
import com.ahancer.rr.models.Campaign;

@Transactional
public interface CampaignDao extends CrudRepository<Campaign, Long> {
	Page<Campaign> findByStatusNotInAndMediaMediaIdIn(Collection<CampaignStatus> statuses, Collection<String> mediaIds, Pageable pageable);
	Page<Campaign> findByBrandId(Long brandId, Pageable pageable);	
	Page<Campaign> findAll(Pageable pageable);
	Campaign findByCampaignIdAndBrandId(Long campaignId, Long brandId);
}