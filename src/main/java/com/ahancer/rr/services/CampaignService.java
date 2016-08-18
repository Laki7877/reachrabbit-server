package com.ahancer.rr.services;

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.CampaignStatus;
import com.ahancer.rr.daos.CampaignDao;
import com.ahancer.rr.daos.CampaignResourceDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.CampaignResource;
import com.ahancer.rr.models.CampaignResourceId;
import com.ahancer.rr.utils.Util;

@Service
@Transactional(rollbackFor=Exception.class)
public class CampaignService {
	@Autowired
	private CampaignDao campaignDao;
	
	@Autowired
	private CampaignResourceDao campaignResourceDao;
	
	public Campaign createCampaignByBrand(Campaign campaign, Brand brand) {
		Set<CampaignResource> resources = campaign.getCampaignResources();
		campaign.setCampaignResources(null);
		campaign.setBrandId(brand.getBrandId());
		campaign.setBrand(null);
		campaign = campaignDao.save(campaign);
		for(CampaignResource resource : resources) {
			resource.setCampaign(campaign);
			CampaignResourceId id = new CampaignResourceId();
			id.setResourceId(resource.getResource().getResourceId());
			id.setCampaignId(campaign.getCampaignId());
			resource.setId(id);
			campaignResourceDao.save(resource);
		}
		campaign.setCampaignResources(resources);
		return campaignDao.save(campaign);
	}

	public Campaign updateCampaignByBrand(Long campaignId, Campaign newCampaign, Brand brand) throws ResponseException {
		Campaign oldCampaign = campaignDao.findByCampaignIdAndBrandId(campaignId, brand.getBrandId());
		if(oldCampaign == null) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.campaign.not.found");
		}
		Util.copyProperties(newCampaign, oldCampaign, "brand", "brandId", "campaignId");
		for(CampaignResource resource : newCampaign.getCampaignResources()) {
			CampaignResourceId id = new CampaignResourceId();
			id.setCampaignId(oldCampaign.getCampaignId());
			id.setResourceId(resource.getResource().getResourceId());
			resource.setId(id);
		}
		oldCampaign.setCampaignResources(newCampaign.getCampaignResources());
		oldCampaign.setMedia(newCampaign.getMedia());
		return campaignDao.save(oldCampaign);
	}
	
	public Page<Campaign> findAll(Pageable pageable) {
		return campaignDao.findAll(pageable);
	}
	
	public Page<Campaign> findAllByBrand(Brand brand, Pageable pageable) {
		return campaignDao.findByBrandId(brand.getBrandId(), pageable);
	}
	
	public Page<Campaign> findAllOpen(String mediaFilter,Pageable pageable) {		
		if(mediaFilter != null) {
			return campaignDao.findByStatusNotInAndMediaMediaIdIn(Arrays.asList(CampaignStatus.Draft, CampaignStatus.Complete), Arrays.asList(mediaFilter), pageable);
		} else {
			return campaignDao.findByStatusNotIn(Arrays.asList(CampaignStatus.Draft, CampaignStatus.Complete), pageable);
		}		
	}
	
	public Campaign findOne(Long id) {
		return campaignDao.findOne(id);
	}
	
	public Campaign findOneByBrand(Long id, Brand brand) {
		return campaignDao.findByCampaignIdAndBrandId(id, brand.getBrandId());
	}
}
