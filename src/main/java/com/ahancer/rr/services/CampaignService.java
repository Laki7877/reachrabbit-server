package com.ahancer.rr.services;

import java.util.Arrays;
import java.util.List;
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

@Service
@Transactional(rollbackFor=Exception.class)
public class CampaignService {
	@Autowired
	private CampaignDao campaignDao;

	@Autowired
	private CampaignResourceDao campaignResourceDao;

	public Campaign createCampaignByBrand(Campaign campaign, Long brandId) {
		Set<CampaignResource> resources = campaign.getCampaignResources();
		campaign.setCampaignResources(null);
		campaign.setBrandId(brandId);
		Brand brand = campaign.getBrand();
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
		campaign.setBrand(brand);
		campaign = campaignDao.save(campaign);
		return campaign;
	}

	public Campaign updateCampaignByBrand(Long campaignId, Campaign newCampaign, Long brandId) throws ResponseException {
		Campaign oldCampaign = campaignDao.findByCampaignIdAndBrandId(campaignId, brandId);
		if(oldCampaign == null) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.campaign.not.found");
		}
		oldCampaign.setTitle(newCampaign.getTitle());
		oldCampaign.setMedia(newCampaign.getMedia());
		oldCampaign.setCategory(newCampaign.getCategory());
		oldCampaign.setDescription(newCampaign.getDescription());
		oldCampaign.setKeyword(newCampaign.getKeyword());
		oldCampaign.setWebsite(newCampaign.getWebsite());
		oldCampaign.setFromBudget(newCampaign.getFromBudget());
		oldCampaign.setToBudget(newCampaign.getToBudget());
		oldCampaign.setProposalDeadline(newCampaign.getProposalDeadline());
		Set<CampaignResource> oldResources = oldCampaign.getCampaignResources();
		for(CampaignResource resource : newCampaign.getCampaignResources()) {
			CampaignResourceId id = new CampaignResourceId();
			id.setCampaignId(oldCampaign.getCampaignId());
			id.setResourceId(resource.getResource().getResourceId());
			resource.setId(id);
			if(oldResources.contains(resource)){
				campaignResourceDao.delete(resource);
			}else {
				campaignResourceDao.save(resource);
			}
		}
		oldCampaign.setCampaignResources(newCampaign.getCampaignResources());
		oldCampaign.setMedia(newCampaign.getMedia());
		return campaignDao.save(oldCampaign);
	}

	public Page<Campaign> findAll(Pageable pageable) {
		return campaignDao.findAll(pageable);
	}

	public Page<Campaign> findAllByBrand(Long brandId, Pageable pageable) {
		return campaignDao.findByBrandId(brandId, pageable);
	}

	public List<Campaign> findAllActiveByBrand(Long brandId) {
		return campaignDao.findByBrandBrandIdAndStatusIn(brandId, Arrays.asList(CampaignStatus.Open, CampaignStatus.Production));
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
