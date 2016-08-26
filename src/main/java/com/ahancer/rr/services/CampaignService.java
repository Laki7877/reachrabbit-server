package com.ahancer.rr.services;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.CampaignResource;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.request.CampaignRequest;

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

	public Campaign createCampaignByBrand(CampaignRequest request, Long brandId) {
		//Setup campaign
		Campaign campaign = new Campaign();
		campaign.setBrandId(brandId);
		campaign.setCategory(request.getCategory());
		campaign.setDescription(request.getDescription());
		campaign.setFromBudget(request.getFromBudget());
		campaign.setKeyword(request.getKeyword());
		campaign.setMedia(request.getMedia());
		campaign.setProposalDeadline(request.getProposalDeadline());
		campaign.setStatus(request.getStatus());
		campaign.setTitle(request.getTitle());
		campaign.setToBudget(request.getToBudget());
		campaign.setWebsite(request.getWebsite());
		campaign = campaignDao.save(campaign);
		//Setup resources
		Set<CampaignResource> resources = request.getCampaignResources();
		for(CampaignResource resource : resources) {
			campaignResourceDao.insertResource(campaign.getCampaignId()
					, resource.getResource().getResourceId()
					, resource.getPosition());
		}
		return findOneByBrand(campaign.getCampaignId(),brandId);
	}

	public Campaign updateCampaignByBrand(Long campaignId, CampaignRequest request, Long brandId, Locale local) throws Exception {
		Campaign campaign = campaignDao.findByCampaignIdAndBrandId(campaignId, brandId);
		if(null == campaign) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.campaign.not.found");
		}
		if(CampaignStatus.Open.equals(campaign.getStatus())){
			if(CampaignStatus.Draft.equals(request.getStatus())){
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.campaign.invalid.status");
			}
		}
		campaign.setBrandId(brandId);
		campaign.setCategory(request.getCategory());
		campaign.setDescription(request.getDescription());
		campaign.setFromBudget(request.getFromBudget());
		campaign.setKeyword(request.getKeyword());
		campaign.setMedia(request.getMedia());
		campaign.setProposalDeadline(request.getProposalDeadline());
		campaign.setStatus(request.getStatus());
		campaign.setTitle(request.getTitle());
		campaign.setToBudget(request.getToBudget());
		campaign.setWebsite(request.getWebsite());
		campaign = campaignDao.save(campaign);
		//Setup resources
		campaignResourceDao.deleteByIdCampaignId(campaignId);
		for(CampaignResource resource : request.getCampaignResources()) {
			campaignResourceDao.insertResource(campaignId, resource.getResource().getResourceId(), resource.getPosition());
		}
		//Setup proposal message
		if(CampaignStatus.Open.equals(campaign.getStatus())){
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
		return findOneByBrand(campaign.getCampaignId(),brandId);
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

	public Campaign findOne(Long campaignId) {
		return campaignDao.findOne(campaignId);
	}

	public Campaign findOneByBrand(Long campaignId, Long brandId) {
		return campaignDao.findByCampaignIdAndBrandId(campaignId, brandId);
	}
}
