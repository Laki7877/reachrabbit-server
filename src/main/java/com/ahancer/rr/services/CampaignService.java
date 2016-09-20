package com.ahancer.rr.services;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.CampaignStatus;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.CampaignDao;
import com.ahancer.rr.daos.CampaignResourceDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.CampaignResource;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.models.User;
import com.ahancer.rr.request.CampaignRequest;
import com.ahancer.rr.response.CampaignResponse;
import com.ahancer.rr.response.UserResponse;

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
	
	@Autowired
	private EmailService emailService;
	
	@Value("${email.admin}")
	private String adminEmail;

	public CampaignRequest createCampaignByBrand(CampaignRequest request, UserResponse user, Locale locale) throws Exception {
		Long brandId = user.getBrand().getBrandId();
		//Setup campaign
		Campaign campaign = new Campaign();
		campaign.setBrandId(brandId);
		campaign.setCategory(request.getCategory());
		campaign.setDescription(request.getDescription());
		campaign.setBudget(request.getBudget());
		campaign.setKeyword(request.getKeyword());
		campaign.setMedia(request.getMedia());
		campaign.setProposalDeadline(request.getProposalDeadline());
		campaign.setStatus(request.getStatus());
		campaign.setTitle(request.getTitle());
		campaign.setWebsite(request.getWebsite());
		campaign.setMainResource(request.getMainResource());
		campaign.setRabbitFlag(false);
		campaign = campaignDao.save(campaign);
		//Setup resources
		Set<CampaignResource> resources = request.getCampaignResources();
		for(CampaignResource resource : resources) {
			campaignResourceDao.insertResource(campaign.getCampaignId()
					, resource.getResource().getResourceId()
					, resource.getPosition());
		}
		validateCampaign(campaign,user,locale);
		request.setCampaignId(campaign.getCampaignId());
		return request;
	}
	
	private void validateCampaign(Campaign campaign, UserResponse user, Locale locale) throws Exception {
		if(CampaignStatus.Open.equals(campaign.getStatus())){
			if(StringUtils.isEmpty(campaign.getTitle())) {
				throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.title.require");
			}
			if(StringUtils.isEmpty(campaign.getDescription())){
				throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.description.require");
			}
			if(null == campaign.getMainResource() || null == campaign.getMainResource().getResourceId()){
				throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.main.resource.require");
			}
			if(null == campaign.getCategory() || null == campaign.getCategory().getCategoryId()){
				throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.category.require");
			}
			if(null == campaign.getMedia() || campaign.getMedia().size() == 0){
				throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.media.require");
			}
			if(null == campaign.getBudget() || null == campaign.getBudget().getBudgetId()){
				throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.budget.require");
			}
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MILLISECOND,0);
			if(null == campaign.getProposalDeadline() || cal.getTime().after(campaign.getProposalDeadline())){
				throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.proposal.deadline.invalid");
			}
			//send email to admin
			String to = adminEmail;
			String subject = messageSource.getMessage("email.admin.brand.publish.campaign.subject",null,locale)
					.replace("{{Category}}", campaign.getCategory().getCategoryName())
					.replace("{{Brand Name}}", user.getBrand().getBrandName());
			String body = messageSource.getMessage("email.admin.brand.publish.campaign.message",null,locale)
					.replace("{{Brand Name}}", user.getBrand().getBrandName())
					.replace("{{Campaign Name}}", campaign.getTitle())
					.replace("{{Category}}", campaign.getCategory().getCategoryName());
			emailService.send(to, subject, body);
		}
	}
	
	public void deleteCampaign(Long campaignId, UserResponse user) throws Exception {
		Long brandId = user.getBrand().getBrandId();
		Campaign campaign = campaignDao.findByCampaignIdAndBrandId(campaignId, brandId);
		if(null == campaign) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.campaign.not.found");
		}
		if(campaign.getProposals().size() > 0){
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.campaign.has.proposal");
		}
		campaignDao.delete(campaignId);
	}

	public CampaignRequest updateCampaign(Long campaignId, CampaignRequest request) {
		Campaign campaign = campaignDao.findOne(campaignId);
		campaign.setStatus(request.getStatus());
		campaign = campaignDao.save(campaign);
		return request;
	}

	public CampaignRequest updateCampaignByBrand(Long campaignId, CampaignRequest request, UserResponse user, Locale locale) throws Exception {
		Long brandId = user.getBrand().getBrandId();
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
		campaign.setBudget(request.getBudget());
		campaign.setKeyword(request.getKeyword());
		campaign.setMedia(request.getMedia());
		campaign.setProposalDeadline(request.getProposalDeadline());
		campaign.setStatus(request.getStatus());
		campaign.setTitle(request.getTitle());
		campaign.setWebsite(request.getWebsite());
		campaign.setMainResource(request.getMainResource());
		campaign = campaignDao.save(campaign);
		//Setup resources
		campaignResourceDao.deleteByIdCampaignId(campaignId);
		for(CampaignResource resource : request.getCampaignResources()) {
			campaignResourceDao.insertResource(campaignId, resource.getResource().getResourceId(), resource.getPosition());
		}
		//Setup proposal message
		if(CampaignStatus.Open.equals(campaign.getStatus())){
			List<Proposal> proposalList = proposalService.findAllByBrand(brandId, campaignId);
			ProposalMessage robotMessage = new ProposalMessage();
			String message = messageSource.getMessage("robot.campaign.message", null, locale).replace("{{Brand Name}}", user.getBrand().getBrandName());
			robotMessage.setMessage(message);
			robotMessage.setCreatedAt(new Date());
			User robotUser = robotService.getRobotUser();
			for(Proposal proposal : proposalList) {
				robotMessage.setProposal(proposal);
				proposalMessageService.createProposalMessage(proposal.getProposalId()
						, robotMessage
						, robotUser.getUserId()
						, robotUser.getRole());
				proposalService.processInboxPollingByOne(proposal.getInfluencerId());
				proposalService.processInboxPollingByOne(proposal.getCampaign().getBrandId());
				proposalMessageService.processMessagePolling(proposal.getProposalId());
			}
		}
		validateCampaign(campaign,user,locale);
		return request;
	}
	
	public Page<CampaignResponse> findAll(Pageable pageable) {
		return campaignDao.findCampaignByAdmin(pageable);
	}
	
	public Page<CampaignResponse> findAllByAdmin(Pageable pageable) {
		return campaignDao.findCampaignByAdmin(pageable);
	}

	public Page<CampaignResponse> findAllByBrand(Long brandId, String statusValue, Pageable pageable) {
		if(StringUtils.isNotEmpty(statusValue)) {
			CampaignStatus status = CampaignStatus.valueOf(statusValue);
			return campaignDao.findByBrandIdAndStatus(brandId, status, pageable);
		} else {
			return campaignDao.findByBrandId(brandId, pageable);
		}
	}

	public List<CampaignResponse> findAllActiveByBrand(Long brandId) {
		return campaignDao.findByBrandIdAndStatus(brandId, Arrays.asList(CampaignStatus.Open));
	}

	public Page<CampaignResponse> findAllOpen(String mediaFilter,Long influencerId,Pageable pageable) {		
		Page<CampaignResponse> response = null;
		if(StringUtils.isNotEmpty(mediaFilter)) {
			response =  campaignDao.findByStatusAndMedia(Arrays.asList(CampaignStatus.Open), Arrays.asList(mediaFilter), pageable);
		} else {
			response = campaignDao.findByStatus(Arrays.asList(CampaignStatus.Open), pageable);
		}
		for(CampaignResponse campaign : response.getContent()){
			for(Proposal proposal : campaign.getProposals()){
				if(proposal.getInfluencerId() == influencerId){
					campaign.setIsApply(true);
					break;
				}
			}
		}
		return response;
	}
	
	public CampaignResponse findOneByInfluencer(Long campaignId, Long influencerId){
		Campaign capaign = campaignDao.findOne(campaignId);
		CampaignResponse response = new CampaignResponse(capaign,Role.Influencer.displayName());
		for(Proposal proposal : response.getProposals()){
			if(proposal.getInfluencerId() == influencerId){
				response.setIsApply(true);
				response.setProposal(proposal);
				break;
			}
		}
		return response;
	}

	public CampaignResponse findOneByAdmin(Long campaignId) {
		Campaign capaign = campaignDao.findOne(campaignId);
		CampaignResponse response = new CampaignResponse(capaign,Role.Admin.displayName());
		return response;
	}

	public CampaignResponse findOneByBrand(Long campaignId, Long brandId) {
		Campaign capaign = campaignDao.findByCampaignIdAndBrandId(campaignId, brandId);
		CampaignResponse response = new CampaignResponse(capaign,Role.Brand.displayName());
		return response;
	}
	
	public void dismissCampaignNotification(Long campaignId, Long brandId){
		campaignDao.updateRabbitFlag(true, campaignId, brandId);
	}
}
