package com.ahancer.rr.services;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;

@Service
@Transactional(rollbackFor=Exception.class)
public class CampaignService {
	
	@Autowired
	private CampaignDao campaignDao;
	
	@Autowired
	private ProposalService proposalService;

	@Autowired
	private CampaignResourceDao campaignResourceDao;
	
	@Autowired
	private ProposalMessageService proposalMessageService;
	
	@Autowired
	private RobotService robotService;
	
	@Autowired
	private MessageSource messageSource;

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

	public Campaign updateCampaignByBrand(Long campaignId, Campaign newCampaign, Long brandId, Locale local) throws Exception {
		Campaign oldCampaign = campaignDao.findByCampaignIdAndBrandId(campaignId, brandId);
		if(null == oldCampaign) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.campaign.not.found");
		}
		if(CampaignStatus.Open.equals(oldCampaign.getStatus())){
			if(CampaignStatus.Draft.equals(newCampaign.getStatus())){
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.campaign.invalid.status");
			}
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
		oldCampaign.setStatus(newCampaign.getStatus());
		Set<CampaignResource> oldResources = oldCampaign.getCampaignResources();
		for(CampaignResource resource : newCampaign.getCampaignResources()) {
			CampaignResourceId id = new CampaignResourceId();
			id.setCampaignId(oldCampaign.getCampaignId());
			id.setResourceId(resource.getResource().getResourceId());
			resource.setId(id);
			if(oldResources.contains(resource)){
				campaignResourceDao.delete(resource.getId());
			}else {
				campaignResourceDao.save(resource);
			}
		}
		if(CampaignStatus.Open.equals(oldCampaign.getStatus())){
			List<Proposal> proposalList = proposalService.findAllByBrand(brandId, campaignId);
			ProposalMessage message = new ProposalMessage();
			message.setMessage(messageSource.getMessage("robot.campaign.message", null, local));
			for(Proposal proposal : proposalList) {
				message.setProposal(proposal);
				proposalMessageService.createProposalMessage(proposal.getProposalId()
						, message
						, robotService.getRobotUser().getUserId()
						, robotService.getRobotUser().getRole());
				proposalService.processInboxPolling(proposal.getInfluencerId());
				proposalService.processInboxPolling(proposal.getCampaign().getBrandId());
				proposalMessageService.processMessagePolling(proposal.getProposalId());
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
