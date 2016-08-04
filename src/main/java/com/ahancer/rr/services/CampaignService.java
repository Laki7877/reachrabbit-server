package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.CampaignDao;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.models.Campaign;

@Service
@Transactional(rollbackFor=Exception.class)
public class CampaignService {
	@Autowired
	private CampaignDao campaignDao;
	
	public Campaign createCampaignByBrand(Campaign campaign, Brand brand) {
		campaign.setBrand(brand);
		return campaignDao.save(campaign);
	}
	
	public Page<Campaign> findAllByBrand(Brand brand, Pageable pageable) {
		return campaignDao.findAllByBrand(brand.getBrandId(), pageable);
	}
	
	public Campaign findCampaignById(Long id) {
		return campaignDao.findOne(id);
	}
}
