package com.ahancer.rr.controllers;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.request.CampaignRequest;
import com.ahancer.rr.services.CampaignService;
import com.ahancer.rr.services.ProposalMessageService;
import com.ahancer.rr.services.ProposalService;

@RestController
@RequestMapping("/campaigns")
public class CampaignController extends AbstractController{
	@Autowired
	private CampaignService campaignService;
	
	@Autowired
	private ProposalService proposalService;
	
	@Autowired
	private ProposalMessageService proposalMessageService;
	
	@RequestMapping(method=RequestMethod.GET)
	public Page<Campaign> getAllCampaign(Pageable pageRequest) throws Exception{
		if(this.getUserRequest().getRole() == Role.Brand) {
			return campaignService.findAllByBrand(this.getUserRequest().getBrand().getBrandId(), pageRequest);	
		} else if(this.getUserRequest().getRole() == Role.Influencer) {
			return campaignService.findAll(pageRequest);		
		}	
		throw new Exception();
	}
	@RequestMapping(value="/active", method=RequestMethod.GET)
	public List<Campaign> getAllActiveCampaign() throws Exception {
		if(this.getUserRequest().getRole() == Role.Brand) {
			return campaignService.findAllActiveByBrand(this.getUserRequest().getBrand().getBrandId());
		}
		throw new Exception();
	}
	
	@RequestMapping(value="/open", method=RequestMethod.GET)
	public Page<Campaign> getOpenCampaign(@RequestParam(name = "mediaId", required=false) String mediaId, Pageable pageRequest) throws Exception {
		return campaignService.findAllOpen(mediaId, pageRequest);
	}
	
	@RequestMapping(value="/{campaignId}",method=RequestMethod.GET)
	public Campaign getOneCampaign(@PathVariable Long campaignId) throws Exception{
		Campaign campaign = campaignService.findOne(campaignId);
		return campaign;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@Authorization(Role.Brand)
	public Campaign createCampaign(@Valid @RequestBody CampaignRequest request) throws Exception{
		return campaignService.createCampaignByBrand(request, this.getUserRequest().getBrand().getBrandId());
	}
	
	@RequestMapping(value="/{campaignId}",method=RequestMethod.PUT)
	public Campaign updateCampaign(@PathVariable Long campaignId,@Valid @RequestBody CampaignRequest request,Locale local) throws Exception{
		return campaignService.updateCampaignByBrand(campaignId, request, this.getUserRequest().getBrand().getBrandId(), local);
	}
	
	@RequestMapping(value="/{campaignId}",method=RequestMethod.DELETE)
	public void deleteCampaign(@PathVariable Long campaignId) throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/{campaignId}/proposals")
	@Authorization(Role.Influencer)
	public Proposal createProposal(@PathVariable Long campaignId,@RequestBody Proposal proposal) throws Exception {
		proposal = proposalService.createCampaignProposalByInfluencer(campaignId, proposal, this.getUserRequest().getInfluencer().getInfluencerId());
		proposalService.processInboxPolling(proposal.getCampaign().getBrandId());
		proposalMessageService.processMessagePolling(proposal.getProposalId());
		return proposal;
	}
	
}
