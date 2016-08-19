package com.ahancer.rr.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.services.ProposalMessageService;
import com.ahancer.rr.services.ProposalMessageService.DeferredProposalMessage;
import com.ahancer.rr.services.ProposalService;

@RestController
@RequestMapping("/proposals")
public class ProposalController extends AbstractController {
	@Autowired
	private ProposalService proposalService;
	
	@Autowired
	private ProposalMessageService proposalMessageService;
	
	@RequestMapping(method=RequestMethod.GET)
	public Page<Proposal> getAllProposal(Pageable pageRequest, @RequestParam(name="campaignId", required=false) Long campaignId) throws Exception{
		if(this.getUserRequest().getRole() == Role.Brand) {
			return proposalService.findAllByBrand(this.getUserRequest().getBrand().getBrandId(), pageRequest);
		} else if(this.getUserRequest().getRole() == Role.Influencer) {
			return proposalService.findAllByInfluencer(this.getUserRequest().getInfluencer().getInfluencerId(), pageRequest);	
		}
		throw new Exception();
	}
	@RequestMapping(method=RequestMethod.GET, value="/active")
	@Authorization(Role.Influencer)
	public List<Proposal> getAllActiveProposal() {
		return proposalService.findAllActiveByInfluencer(this.getUserRequest().getInfluencer().getInfluencerId());
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/{proposalId}/proposalmessages")
	@Authorization({Role.Admin,Role.Brand,Role.Influencer})
	public ProposalMessage createProposalMessage(@PathVariable Long proposalId,@RequestBody ProposalMessage message) throws Exception {
		message = proposalMessageService.createProposalMessage(proposalId,message, this.getUserRequest().getUserId(),this.getUserRequest().getRole());
		return message;
	}
	
	@RequestMapping(method=RequestMethod.PUT,value="/{proposalId}")
	@Authorization(Role.Influencer)
	public Proposal updateProposal(@PathVariable Long proposalId,@RequestBody Proposal proposal) throws Exception {
		return proposalService.updateCampaignProposalByInfluencer(proposalId, proposal, this.getUserRequest().getInfluencer().getInfluencerId());
	}
	
	
	@RequestMapping(method=RequestMethod.GET,value="/{proposalId}/proposalmessages")
	@Authorization(value={Role.Admin,Role.Brand,Role.Influencer})
	public Page<ProposalMessage> getAllProposalMessage(@PathVariable Long proposalId,Pageable pageRequest) throws Exception {
		Page<ProposalMessage> messages = proposalMessageService.findByProposal(proposalId, pageRequest);
		return messages;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{proposalId}/proposalmessages/poll")
	@Authorization(value={Role.Admin,Role.Brand,Role.Influencer})
	public @ResponseBody DeferredProposalMessage getAllProposalMessagePoll(@PathVariable Long proposalId, Pageable pageRequest) {
		DeferredProposalMessage result = new DeferredProposalMessage(proposalId, pageRequest);
		proposalMessageService.addPollingQueue(result);
		return result;
	}
	@Scheduled(fixedRate=2000)
	public void processPollingQueue() {
		proposalMessageService.processPollingQueue();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/{proposalId}")
	public Proposal getOneProposal(@PathVariable Long proposalId) throws Exception {
		if(this.getUserRequest().getRole() == Role.Brand) {
			return proposalService.findOneByBrand(proposalId,this.getUserRequest().getBrand().getBrandId());
		} else if(this.getUserRequest().getRole() == Role.Influencer) {
			return proposalService.findOneByInfluencer(proposalId,this.getUserRequest().getInfluencer().getInfluencerId());		
		}
		throw new Exception();
	}
	
	

}
