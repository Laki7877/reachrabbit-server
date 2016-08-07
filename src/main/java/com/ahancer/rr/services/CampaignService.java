package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.CampaignDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.utils.Util;

@Service
@Transactional(rollbackFor=Exception.class)
public class CampaignService {
	@Autowired
	private CampaignDao campaignDao;
	
	
	public Campaign createCampaignByBrand(Campaign campaign, Brand brand) {
		//campaign.setBrand(brand);
		campaign.setBrandId(brand.getBrandId());
		campaign.setBrand(brand);
		//campaign.setCategoryId(campaign.getCategory().getCategoryId());
		return campaignDao.save(campaign);
	}

	public Campaign updateCampaignByBrand(Long campaignId, Campaign newCampaign, Brand brand) throws ResponseException {
		Campaign oldCampaign = campaignDao.findOneWithBrandId(campaignId, brand.getBrandId());
		if(oldCampaign == null) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.campaign.not.found");
		}
		Util.copyProperties(newCampaign, oldCampaign, "brand", "brandId", "campaignId");
		return campaignDao.save(oldCampaign);
	}
	
	public Page<Campaign> findAllByBrand(Brand brand, Pageable pageable) {
		return campaignDao.findAllByBrand(brand.getBrandId(), pageable);
	}
	
	public Campaign findOneByBrand(Long id, Brand brand) {
		return campaignDao.findOneWithBrandId(id, brand.getBrandId());
	}
}
