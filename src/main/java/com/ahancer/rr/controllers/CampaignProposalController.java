package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.CampaignProposal;
import com.ahancer.rr.services.CampaignProposalService;

@RestController
@RequestMapping("/campaignproposals")
public class CampaignProposalController extends AbstractController {
	
	
	@Autowired
	private CampaignProposalService campaignProposalService;
	
	
	@RequestMapping(method=RequestMethod.GET)
	public Page<CampaignProposal> getAllCampaign(Pageable pageRequest) throws Exception{
		if(this.getUserRequest().getRole() == Role.Brand) {
			return null;
			//return campaignService.findAllByBrand(this.getUserRequest().getBrand(), pageRequest);	
		} else if(this.getUserRequest().getRole() == Role.Influencer) {
			return null;
			//return campaignService.findAll(pageRequest);		
		}	
		throw new Exception();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@Authorization(Role.Influencer)
	public CampaignProposal createCampaign(@RequestBody CampaignProposal proposal) throws Exception {
		return campaignProposalService.createCampaignProposalByInfluencer(proposal, this.getUserRequest().getInfluencer());
	}

}
