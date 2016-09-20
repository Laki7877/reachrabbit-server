package com.ahancer.rr.controllers;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Cart;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.response.ProposalCountResponse;
import com.ahancer.rr.response.ProposalResponse;
import com.ahancer.rr.services.CartService;
import com.ahancer.rr.services.ProposalMessageService;
import com.ahancer.rr.services.ProposalMessageService.DeferredProposalMessage;
import com.ahancer.rr.services.ProposalService;
import com.ahancer.rr.services.ProposalService.DeferredProposal;
import com.ahancer.rr.utils.Util;

@RestController
@RequestMapping("/proposals")
public class ProposalController extends AbstractController {
	@Autowired
	private ProposalService proposalService;
	
	@Autowired
	private ProposalMessageService proposalMessageService;
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping(method=RequestMethod.GET)
	@Authorization({Role.Influencer,Role.Brand})
	public Page<Proposal> getAllProposal( @RequestParam(name="status", required=true) ProposalStatus status,Pageable pageRequest, @RequestParam(name="campaignId", required=false) Long campaignId) throws Exception{
		if(Role.Brand.equals(this.getUserRequest().getRole())) {
			return proposalService.findAllByBrand(this.getUserRequest().getBrand().getBrandId(),status, pageRequest);
		} else if(Role.Influencer.equals(this.getUserRequest().getRole())) {
			return proposalService.findAllByInfluencer(this.getUserRequest().getInfluencer().getInfluencerId(),status, pageRequest);	
		}
		throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
	}
	@RequestMapping(method=RequestMethod.GET, value="/active")
	@Authorization({Role.Influencer})
	public List<Proposal> getAllActiveProposal() {
		return proposalService.findAllActiveByInfluencer(this.getUserRequest().getInfluencer().getInfluencerId());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/count/poll")
	public DeferredProposal getAllUnreadProposal(@RequestParam(name="immediate", required=false) Boolean immediate) throws Exception {
		final DeferredProposal result = new DeferredProposal(this.getUserRequest().getRole());
		proposalService.addInboxPolling(this.getUserRequest().getUserId(), result);
		if(true == immediate) {
			proposalService.processInboxPolling(this.getUserRequest().getUserId());
		}
		return result;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/count")
	@Authorization({Role.Influencer,Role.Brand})
	public ProposalCountResponse getProposalCountByStatus(@RequestParam("status") ProposalStatus status) throws Exception {
		if(Role.Brand.equals(this.getUserRequest().getRole())) {
			return proposalService.countByBrand(this.getUserRequest().getBrand().getBrandId(), status);
		} else if(Role.Influencer.equals(this.getUserRequest().getRole())) {
			return proposalService.countByInfluencer(this.getUserRequest().getInfluencer().getInfluencerId(), status);
		}
		throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{proposalId}/proposalmessages/count")
	public Long getUnreadProposalMessageCount(@PathVariable Long proposalId) throws Exception {
		if(Role.Brand.equals(this.getUserRequest().getRole())) {
			return proposalService.countByUnreadProposalMessageForBrand(proposalId, this.getUserRequest().getBrand().getBrandId());			
		} else if(Role.Influencer.equals(this.getUserRequest().getRole())) {
			return proposalService.countByUnreadProposalMessageForInfluencer(proposalId, this.getUserRequest().getInfluencer().getInfluencerId());
		}
		throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/{proposalId}/proposalmessages")
	@Authorization({Role.Admin,Role.Brand,Role.Influencer})
	public ProposalMessage createProposalMessage(@PathVariable Long proposalId, @RequestBody ProposalMessage message) throws Exception {
		message = proposalMessageService.createProposalMessage(proposalId,message, this.getUserRequest().getUserId(), this.getUserRequest().getRole());
		proposalMessageService.processMessagePolling(proposalId);
		
		//For brand, notify influencer
		if(Role.Brand.equals(this.getUserRequest().getRole())) {
			proposalService.processInboxPolling(message.getProposal().getInfluencerId());
		}
		//For influencer, notify brand
		else if(Role.Influencer.equals(this.getUserRequest().getRole())) {
			proposalService.processInboxPolling(message.getProposal().getCampaign().getBrandId());
		}
		
		return message;
	}
	
	@RequestMapping(method=RequestMethod.PUT,value="/{proposalId}")
	@Authorization(Role.Influencer)
	public Proposal updateProposal(@PathVariable Long proposalId,@RequestBody Proposal proposal
			,@RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		proposal = proposalService.updateCampaignProposalByInfluencer(proposalId, proposal, this.getUserRequest().getInfluencer().getInfluencerId(),locale);
		proposalService.processInboxPolling(proposal.getInfluencerId());
		proposalService.processInboxPolling(proposal.getCampaign().getBrandId());
		proposalMessageService.processMessagePolling(proposal.getProposalId());
		return proposal;
	}
	
	@RequestMapping(method=RequestMethod.PUT,value="/{proposalId}/dismiss")
	@Authorization(Role.Influencer)
	public void dismissNotification(@PathVariable Long proposalId) throws Exception {
		proposalService.dismissProposalNotification(proposalId,this.getUserRequest().getInfluencer().getInfluencerId());
	}
	
	
	@RequestMapping(method=RequestMethod.GET,value="/{proposalId}/proposalmessages")
	@Authorization({Role.Admin,Role.Brand,Role.Influencer})
	public Page<ProposalMessage> getAllProposalMessage(@PathVariable Long proposalId, @RequestParam(name="timestamp", required=false) String timestamp, Pageable pageRequest) throws Exception {
		Page<ProposalMessage> result = null;
		if(Role.Brand.equals(this.getUserRequest().getRole())) {
			result = proposalMessageService.findByProposalForBrand(proposalId, this.getUserRequest().getBrand().getBrandId(), Util.parseJacksonDate(timestamp), pageRequest);
		} else if(Role.Influencer.equals(this.getUserRequest().getRole())) {
			result = proposalMessageService.findByProposalForInfluencer(proposalId, this.getUserRequest().getInfluencer().getInfluencerId(), Util.parseJacksonDate(timestamp), pageRequest);
		}
		proposalService.processInboxPolling(this.getUserRequest().getUserId());
		return result;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{proposalId}/proposalmessages/new")
	public List<ProposalMessage> getNewProposalMessage(@PathVariable Long proposalId, @RequestParam(name="timestamp") String timestamp) throws Exception {
		return proposalMessageService.getNewProposalMessage(proposalId,this.getUserRequest().getRole() ,Util.parseJacksonDate(timestamp));
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{proposalId}/proposalmessages/poll")
	@Authorization({Role.Admin,Role.Brand,Role.Influencer})
	public @ResponseBody DeferredProposalMessage getAllProposalMessagePoll(@PathVariable Long proposalId, @RequestParam(name="timestamp",required=false)  String timestamp ) throws Exception {
		final Date date = Util.parseJacksonDate(timestamp);
		final DeferredProposalMessage result = new DeferredProposalMessage(proposalId, date, this.getUserRequest().getRole());
		System.out.println(date);
		Long count = proposalMessageService.countNewProposalMessage(proposalId, date);
		System.out.println(count);
		if(count > 0L) {
			result.setResult(date);
		} else {
			proposalMessageService.addMessagePolling(proposalId, result);
		}
		return result;
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/{proposalId}")
	@Authorization({Role.Brand,Role.Influencer})
	public ProposalResponse getOneProposal(@PathVariable Long proposalId) throws Exception {
		if(Role.Brand.equals(this.getUserRequest().getRole())) {
			return proposalService.findOneByBrand(proposalId,this.getUserRequest().getBrand().getBrandId());
		} else if(Role.Influencer.equals(this.getUserRequest().getRole())) {
			return proposalService.findOneByInfluencer(proposalId,this.getUserRequest().getInfluencer().getInfluencerId());		
		}
		throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
	}
	
	@RequestMapping(method=RequestMethod.PUT,value="/{proposalId}/status/{status}")
	@Authorization({Role.Brand})
	public Proposal updateProposalStatus(@PathVariable Long proposalId,@PathVariable ProposalStatus status
			,@RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		Proposal proposal = proposalService.updateProposalStatusByBrand(proposalId, status, this.getUserRequest().getBrand().getBrandId(), locale);
		proposalService.processInboxPollingByOne(proposal.getInfluencerId());
		proposalService.processInboxPollingByOne(proposal.getCampaign().getBrandId());
		proposalMessageService.processMessagePolling(proposal.getProposalId());
		return proposal;
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/{proposalId}/cart")
	@Authorization({Role.Brand})
	public Cart addProposalToCaert(@PathVariable Long proposalId) throws Exception {
		Cart cart = cartService.addProposalToCart(proposalId, this.getUserRequest().getBrand().getBrandId());
		return cart;
	}
	
	
	@RequestMapping(method=RequestMethod.DELETE,value="/{proposalId}/cart")
	@Authorization({Role.Brand})
	public void deleteProposalToCaert(@PathVariable Long proposalId) throws Exception {
		cartService.deleteProposalToCart(proposalId, this.getUserRequest().getBrand().getBrandId());
	}
	
	

}
