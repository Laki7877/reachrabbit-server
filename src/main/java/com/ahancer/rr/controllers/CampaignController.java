package com.ahancer.rr.controllers;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.request.CampaignRequest;
import com.ahancer.rr.response.CampaignResponse;
import com.ahancer.rr.services.CampaignService;
import com.ahancer.rr.services.ProposalMessageService;
import com.ahancer.rr.services.ProposalService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/campaigns")
public class CampaignController extends AbstractController {
	@Autowired
	private CampaignService campaignService;
	
	@Autowired
	private ProposalService proposalService;
	
	@Autowired
	private ProposalMessageService proposalMessageService;
	
	@RequestMapping(method=RequestMethod.GET)
	public Page<CampaignResponse> getAllCampaign(Pageable pageRequest) throws Exception{
		if(this.getUserRequest().getRole().equals(Role.Brand)) {
			return campaignService.findAllByBrand(this.getUserRequest().getBrand().getBrandId(), pageRequest);	
		} else if(this.getUserRequest().getRole().equals(Role.Influencer)) {
			return campaignService.findAll(pageRequest);		
		} else if(this.getUserRequest().getRole().equals(Role.Admin)) {
			return campaignService.findAllByAdmin(pageRequest);
		}
		throw new Exception();
	}
	
	@ApiOperation(value = "Get campaign by campaign id")
	@RequestMapping(value="/active", method=RequestMethod.GET)
	@Authorization(Role.Brand)
	public List<CampaignResponse> getAllActiveCampaign() throws Exception {
		return campaignService.findAllActiveByBrand(this.getUserRequest().getBrand().getBrandId());
	}
	
	@RequestMapping(value="/open", method=RequestMethod.GET)
	@Authorization(Role.Influencer)
	public Page<CampaignResponse> getOpenCampaign(@RequestParam(name = "mediaId", required=false) String mediaId, Pageable pageRequest) throws Exception {
		return campaignService.findAllOpen(mediaId, pageRequest);
	}
	
	@ApiOperation(value = "Get campaign by campaign id")
	@RequestMapping(value="/{campaignId}",method=RequestMethod.GET)
	public Campaign getOneCampaign(@PathVariable Long campaignId) throws Exception{
		Campaign campaign = campaignService.findOne(campaignId);
		return campaign;
	}
	
	
	@ApiOperation(value = "Create new campaign")
	@RequestMapping(method=RequestMethod.POST)
	@Authorization(Role.Brand)
	public Campaign createCampaign(@Valid @RequestBody CampaignRequest request
			,@RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		Campaign campaign = campaignService.createCampaignByBrand(request, this.getUserRequest(),locale);
		return getOneCampaign(campaign.getCampaignId());
	}
	
	@RequestMapping(value="/{campaignId}",method=RequestMethod.PUT)
	public Campaign updateCampaign(@PathVariable Long campaignId,@Valid @RequestBody CampaignRequest request
			,@RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		
				
		Campaign campaign = null;

		//Admin powered
		if(this.getUserRequest().getRole().equals(Role.Admin)) {
			campaign = campaignService.updateCampaign(campaignId, request);
		} else {
			campaign = campaignService.updateCampaignByBrand(campaignId, request, this.getUserRequest(), locale);
		}
		
		return getOneCampaign(campaign.getCampaignId());
	}
	
	@RequestMapping(value="/{campaignId}",method=RequestMethod.DELETE)
	@Authorization(Role.Brand)
	public void deleteCampaignByBrand(@PathVariable Long campaignId) throws Exception {
		campaignService.deleteCampaign(campaignId, this.getUserRequest());
	}
	
	@RequestMapping(value="/{campaignId}/dismiss",method=RequestMethod.PUT)
	public void dismissCampaignNotification(@PathVariable Long campaignId) throws Exception {
		campaignService.dismissCampaignNotification(campaignId, this.getUserRequest().getBrand().getBrandId());
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/{campaignId}/proposals")
	@Authorization(Role.Influencer)
	public Proposal createProposal(@PathVariable Long campaignId,@RequestBody Proposal proposal
			,@RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		proposal = proposalService.createCampaignProposalByInfluencer(campaignId, proposal, this.getUserRequest(),locale);
		proposalService.processInboxPolling(proposal.getCampaign().getBrandId());
		proposalMessageService.processMessagePolling(proposal.getProposalId());
		return proposal;
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/{campaignId}/applied")
	@Authorization(Role.Influencer)
	public Proposal getAppliedProposal(@PathVariable Long campaignId) throws Exception {
		return proposalService.getAppliedProposal(this.getUserRequest().getInfluencer().getInfluencerId(),campaignId);
	}
	
	
	
}
