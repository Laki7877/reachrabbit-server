package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.services.ProposalService;

@RestController
@RequestMapping("/proposals")
public class ProposalController extends AbstractController {
	
	
	@Autowired
	private ProposalService proposalService;
	
	
	@RequestMapping(method=RequestMethod.GET)
	public Page<Proposal> getAllCampaign(Pageable pageRequest) throws Exception{
		if(this.getUserRequest().getRole() == Role.Brand) {
			return null;
			//return campaignService.findAllByBrand(this.getUserRequest().getBrand(), pageRequest);	
		} else if(this.getUserRequest().getRole() == Role.Influencer) {
			return null;
			//return campaignService.findAll(pageRequest);		
		}	
		throw new Exception();
	}

}
