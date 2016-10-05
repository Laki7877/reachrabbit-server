package com.ahancer.rr.controllers;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Cart;
import com.ahancer.rr.models.Post;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.request.PostRequest;
import com.ahancer.rr.response.ProposalCountResponse;
import com.ahancer.rr.response.ProposalMessageResponse;
import com.ahancer.rr.response.ProposalResponse;
import com.ahancer.rr.response.UpdatePostResponse;
import com.ahancer.rr.services.CartService;
import com.ahancer.rr.services.PostService;
import com.ahancer.rr.services.ProposalMessageService;
import com.ahancer.rr.services.ProposalMessageService.DeferredProposalMessage;
import com.ahancer.rr.services.ProposalService;
import com.ahancer.rr.services.ProposalService.DeferredProposal;
import com.ahancer.rr.utils.Util;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/proposals")
public class ProposalController extends AbstractController {
	@Autowired
	private PostService postService;
	@Autowired
	private ProposalService proposalService;
	@Autowired
	private ProposalMessageService proposalMessageService;
	@Autowired
	private CartService cartService;
	@ApiOperation(value = "Get proposal pagination")
	@RequestMapping(method=RequestMethod.GET)
	@Authorization({Role.Influencer,Role.Brand, Role.Admin})
	public Page<Proposal> getAllProposal(@RequestParam(name="status", required=true) ProposalStatus status
			, @RequestParam(name="search", required=false) String search,Pageable pageRequest, @RequestParam(name="campaignId", required=false) Long campaignId) throws Exception{
		Page<Proposal> response = null;
		switch(this.getUserRequest().getRole()){
		case Brand:
			response = proposalService.findAllByBrand(this.getUserRequest().getBrand().getBrandId(),status, search, pageRequest);
			break;
		case Influencer:
			response = proposalService.findAllByInfluencer(this.getUserRequest().getInfluencer().getInfluencerId(),status, search, pageRequest);
			break;
		case Admin:
			response = proposalService.findAllByAdmin(status, search, pageRequest);
			break;
		default:
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
		}
		return response;
	}
	@ApiOperation(value = "Get active proposal list")
	@RequestMapping(method=RequestMethod.GET, value="/active")
	@Authorization({Role.Influencer})
	public List<Proposal> getAllActiveProposal() {
		return proposalService.findAllActiveByInfluencer(this.getUserRequest().getInfluencer().getInfluencerId());
	}
	@ApiOperation(value = "Get count new message")
	@RequestMapping(method=RequestMethod.GET, value="/count/poll")
	public DeferredProposal getAllUnreadProposal(@RequestParam(name="immediate", required=false) Boolean immediate) throws Exception {
		final DeferredProposal result = new DeferredProposal(this.getUserRequest().getRole());
		proposalService.addInboxPolling(this.getUserRequest().getUserId(), result);
		if(true == immediate) {
			proposalService.processInboxPolling(this.getUserRequest().getUserId());
		}
		return result;
	}
	@ApiOperation(value = "Get count proposal status")
	@RequestMapping(method=RequestMethod.GET, value="/count")
	@Authorization({Role.Influencer,Role.Brand, Role.Admin})
	public ProposalCountResponse getProposalCountByStatus(@RequestParam("status") ProposalStatus status) throws Exception {
		ProposalCountResponse response = null;
		switch(this.getUserRequest().getRole()){
		case Brand:
			response = proposalService.countByBrand(this.getUserRequest().getBrand().getBrandId(), status);
			break;
		case Influencer:
			response = proposalService.countByInfluencer(this.getUserRequest().getInfluencer().getInfluencerId(), status);
			break;
		case Admin:
			response = proposalService.countByAdmin(status);
			break;
		default:
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
		}
		return response;
	}
	@ApiOperation(value = "Get count proposal message from proposal")
	@RequestMapping(method=RequestMethod.GET, value="/{proposalId}/proposalmessages/count")
	public Long getUnreadProposalMessageCount(@PathVariable Long proposalId) throws Exception {
		Long response = null;
		switch(this.getUserRequest().getRole()){
		case Brand:
			response = proposalService.countByUnreadProposalMessageForBrand(proposalId, this.getUserRequest().getBrand().getBrandId());			
			break;
		case Influencer:
			response = proposalService.countByUnreadProposalMessageForInfluencer(proposalId, this.getUserRequest().getInfluencer().getInfluencerId());
			break;
		default:
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
		}
		return response;
	}
	@ApiOperation(value = "Create new proposal message in proposal")
	@RequestMapping(method=RequestMethod.POST,value="/{proposalId}/proposalmessages")
	@Authorization({Role.Admin,Role.Brand,Role.Influencer})
	public ProposalMessageResponse createProposalMessage(@PathVariable Long proposalId, @RequestBody ProposalMessage message) throws Exception {
		message = proposalMessageService.createProposalMessage(proposalId,message, this.getUserRequest().getUserId(), this.getUserRequest().getRole());
		proposalMessageService.processMessagePolling(proposalId);
		switch(this.getUserRequest().getRole()){
		case Brand:
			proposalService.processInboxPolling(message.getProposal().getInfluencerId());	
			break;
		case Influencer:
			proposalService.processInboxPolling(message.getProposal().getCampaign().getBrandId());
			break;
		default:
			break;
		}
		return new ProposalMessageResponse(message,this.getUserRequest().getRole().displayName());
	}
	@ApiOperation(value = "Update proposal")
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
	@ApiOperation(value = "Update rabbit flag in proposal")
	@RequestMapping(method=RequestMethod.PUT,value="/{proposalId}/dismiss")
	@Authorization(Role.Influencer)
	public void dismissNotification(@PathVariable Long proposalId) throws Exception {
		proposalService.dismissProposalNotification(proposalId,this.getUserRequest().getInfluencer().getInfluencerId());
	}
	@ApiOperation(value = "Get proposal message pagination")
	@RequestMapping(method=RequestMethod.GET,value="/{proposalId}/proposalmessages")
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public Page<ProposalMessageResponse> getAllProposalMessage(@PathVariable Long proposalId, @RequestParam(name="timestamp", required=false) String timestamp, Pageable pageRequest) throws Exception {
		Page<ProposalMessageResponse> response = null;
		switch(this.getUserRequest().getRole()){
		case Brand:
			response = proposalMessageService.findByProposalForBrand(proposalId, this.getUserRequest().getBrand().getBrandId(), Util.parseJacksonDate(timestamp), pageRequest);
			break;
		case Influencer:
			response = proposalMessageService.findByProposalForInfluencer(proposalId, this.getUserRequest().getInfluencer().getInfluencerId(), Util.parseJacksonDate(timestamp), pageRequest);
			break;
		case Admin:
			response = proposalMessageService.findByAdmin(proposalId,Util.parseJacksonDate(timestamp), pageRequest);
			break;
		default:
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
		}
		return response;

	}
	@ApiOperation(value = "Get new proposal message list")
	@RequestMapping(method=RequestMethod.GET, value="/{proposalId}/proposalmessages/new")
	public List<ProposalMessageResponse> getNewProposalMessage(@PathVariable Long proposalId, @RequestParam(name="timestamp") String timestamp) throws Exception {
		List<ProposalMessageResponse> response = null;
		Date date = Util.parseJacksonDate(timestamp);
		switch(this.getUserRequest().getRole()){
		case Brand:
			response = proposalMessageService.getNewProposalMessageByBrand(proposalId,this.getUserRequest().getBrand().getBrandId() , date);
			break;
		case Admin:
			break;
		case Influencer:
			response = proposalMessageService.getNewProposalMessageByInfluencer(proposalId, this.getUserRequest().getInfluencer().getInfluencerId(), date);
			break;
		default:
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
		}
		return response;
	}
	@ApiOperation(value = "Get new proposal message list")
	@RequestMapping(method=RequestMethod.GET, value="/{proposalId}/proposalmessages/poll")
	@Authorization({Role.Admin,Role.Brand,Role.Influencer})
	public DeferredProposalMessage getAllProposalMessagePoll(@PathVariable Long proposalId, @RequestParam(name="timestamp",required=false)  String timestamp ) throws Exception {
		Date date = new Date();
		if(StringUtils.isNotEmpty(timestamp)){
			date = Util.parseJacksonDate(timestamp);
		}
		final DeferredProposalMessage result = new DeferredProposalMessage(proposalId, date, this.getUserRequest().getRole());
		Long count = proposalMessageService.countNewProposalMessage(proposalId, date);
		if(count > 0L) {
			result.setResult(date);
		} else {
			proposalMessageService.addMessagePolling(proposalId, result);
		}
		return result;
	}
	@ApiOperation(value = "Get proposal from proposal id")
	@RequestMapping(method=RequestMethod.GET,value="/{proposalId}")
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public ProposalResponse getOneProposal(@PathVariable Long proposalId) throws Exception {
		ProposalResponse response = null;
		switch(this.getUserRequest().getRole()){
		case Brand:
			response = proposalService.findOneByBrand(proposalId,this.getUserRequest().getBrand().getBrandId());
			break;
		case Influencer:
			response = proposalService.findOneByInfluencer(proposalId,this.getUserRequest().getInfluencer().getInfluencerId());
			break;
		case Admin:
			response = proposalService.findOneByAdmin(proposalId);
			break;
		default:
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
		}
		return response;
	}
	@ApiOperation(value = "Update proposal status")
	@RequestMapping(method=RequestMethod.PUT,value="/{proposalId}/status/{status}")
	@Authorization({Role.Brand})
	public Proposal updateProposalStatus(@PathVariable Long proposalId,@PathVariable ProposalStatus status
			,@RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		Proposal proposal = proposalService.updateProposalStatusByBrand(proposalId, status, this.getUserRequest().getBrand().getBrandId(), locale);
		proposalService.processInboxPolling(proposal.getInfluencerId());
		proposalService.processInboxPolling(proposal.getCampaign().getBrandId());
		proposalMessageService.processMessagePolling(proposal.getProposalId());
		return proposal;
	}
	@ApiOperation(value = "Add proposal to cart")
	@RequestMapping(method=RequestMethod.POST,value="/{proposalId}/cart")
	@Authorization({Role.Brand})
	public Cart addProposalToCaert(@PathVariable Long proposalId) throws Exception {
		Cart cart = cartService.addProposalToCart(proposalId, this.getUserRequest().getBrand().getBrandId());
		proposalService.processInboxPolling(this.getUserRequest().getBrand().getBrandId());
		return cart;
	}
	@ApiOperation(value = "Delete proposal from cart")
	@RequestMapping(method=RequestMethod.DELETE,value="/{proposalId}/cart")
	@Authorization({Role.Brand})
	public void deleteProposalFromCart(@PathVariable Long proposalId) throws Exception {
		cartService.deleteProposalToCart(proposalId, this.getUserRequest().getBrand().getBrandId());
		proposalService.processInboxPolling(this.getUserRequest().getBrand().getBrandId());
	}
	@ApiOperation(value = "Add post to proposal")
	@RequestMapping(method=RequestMethod.POST,value="/{proposalId}/post")
	@Authorization({Role.Admin})
	public Post addPostToProposal(@PathVariable Long proposalId, @RequestBody PostRequest request) throws Exception {
		return postService.createPostByAdmin(proposalId, request);
	}
	@ApiOperation(value = "Get post from proposal")
	@RequestMapping(method=RequestMethod.GET,value="/{proposalId}/post")
	@Authorization({Role.Admin})
	public List<UpdatePostResponse> getListOfPost(@PathVariable Long proposalId){
		return postService.getListPost(proposalId);
	}
	@ApiOperation(value = "Delete post from proposal")
	@RequestMapping(method=RequestMethod.DELETE,value="/{proposalId}/post")
	@Authorization({Role.Admin})
	public int deletePostToProposal(@PathVariable Long proposalId, @RequestBody PostRequest request) throws Exception {
		return postService.deletePost(proposalId, request);
	}
	
}
