package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.ahancer.rr.daos.CampaignDao;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.models.Campaign;

public class CampaignService {
	@Autowired
	private CampaignDao campaignDao;
	
	public Campaign createCampaignByBrand(Campaign campaign, Brand brand) {
		campaign.setBrand(brand);
		return campaignDao.save(campaign);
	}
	
	public Iterable<Campaign> findAndCountByBrandCampaign(Brand brand) {
		return campaignDao.findAll();
	}
	
	public Campaign findCampaignById(Long id) {
		return campaignDao.findOne(id);
	}
}
