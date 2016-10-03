package com.ahancer.rr.controllers;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

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
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.exception.ResponseException;
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
	@ApiOperation(value = "Get campaign pagination")
	@RequestMapping(method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public Page<CampaignResponse> getAllCampaign(@RequestParam(name="status",required=false) String status,Pageable pageRequest) throws Exception {
		Page<CampaignResponse> response = null;
		switch(this.getUserRequest().getRole()){
		case Brand:
			response = campaignService.findAllByBrand(this.getUserRequest().getBrand().getBrandId(), status, pageRequest);
			break;
		case Influencer:
			response = campaignService.findAll(pageRequest);
			break;
		case Admin:
			response = campaignService.findAllByAdmin(pageRequest);
			break;
		default:
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
		}
		return response;
	}
	@ApiOperation(value = "Get active campaign")
	@RequestMapping(value="/active", method=RequestMethod.GET)
	@Authorization({Role.Brand})
	public List<CampaignResponse> getAllActiveCampaign() throws Exception {
		return campaignService.findAllActiveByBrand(this.getUserRequest().getBrand().getBrandId());
	}
	@ApiOperation(value = "Get open campaign pagination")
	@RequestMapping(value="/open", method=RequestMethod.GET)
	@Authorization({Role.Influencer})
	public Page<CampaignResponse> getOpenCampaign(@RequestParam(name = "mediaId", required=false) String mediaId, Pageable pageRequest) throws Exception {
		return campaignService.findAllOpen(mediaId,this.getUserRequest().getInfluencer().getInfluencerId(), pageRequest);
	}
	@ApiOperation(value = "Get campaign by campaign id")
	@RequestMapping(value="/{campaignId}",method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public CampaignResponse getOneCampaign(@PathVariable Long campaignId) throws Exception {
		CampaignResponse response = null;
		switch(this.getUserRequest().getRole()){
		case Brand:
			response = campaignService.findOneByBrand(campaignId,this.getUserRequest().getBrand().getBrandId());
			break;
		case Influencer:
			response = campaignService.findOneByInfluencer(campaignId, this.getUserRequest().getInfluencer().getInfluencerId());
			break;
		case Admin:
			response = campaignService.findOneByAdmin(campaignId);
			break;
		default:
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
		}
		return response;
	}
	@ApiOperation(value = "Get campaign by campaign id")
	@RequestMapping(value="/public/{publicCode}",method=RequestMethod.GET)
	public CampaignResponse getOnePublicCampaign(@PathVariable String publicCode) throws Exception {
		CampaignResponse response = null;
		response = campaignService.findOneByPublic(publicCode);
		return response;
	}
	@ApiOperation(value = "Create new campaign")
	@RequestMapping(method=RequestMethod.POST)
	@Authorization({Role.Brand})
	public CampaignRequest createCampaign(@Valid @RequestBody CampaignRequest request
			,@RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		return campaignService.createCampaignByBrand(request, this.getUserRequest(),locale);
	}
	@ApiOperation(value = "Update campaign")
	@RequestMapping(value="/{campaignId}",method=RequestMethod.PUT)
	@Authorization({Role.Brand,Role.Admin})
	public CampaignRequest updateCampaign(@PathVariable Long campaignId,@Valid @RequestBody CampaignRequest request
			,@RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		CampaignRequest response = null;
		switch(this.getUserRequest().getRole()){
		case Brand:
			response = campaignService.updateCampaignByBrand(campaignId, request, this.getUserRequest(), locale);
			break;
		case Admin:
			response = campaignService.updateCampaign(campaignId, request);
			break;
		default:
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
		}
		return response;
	}
	@ApiOperation(value = "Delete campaign")
	@RequestMapping(value="/{campaignId}",method=RequestMethod.DELETE)
	@Authorization({Role.Brand})
	public void deleteCampaignByBrand(@PathVariable Long campaignId) throws Exception {
		campaignService.deleteCampaign(campaignId, this.getUserRequest());
	}
	@ApiOperation(value = "Uadate rabbit flag in campaign")
	@RequestMapping(value="/{campaignId}/dismiss",method=RequestMethod.PUT)
	@Authorization({Role.Brand})
	public void dismissCampaignNotification(@PathVariable Long campaignId) throws Exception {
		campaignService.dismissCampaignNotification(campaignId, this.getUserRequest().getBrand().getBrandId());
	}
	@ApiOperation(value = "Create new proposal from campaign")
	@RequestMapping(method=RequestMethod.POST,value="/{campaignId}/proposals")
	@Authorization({Role.Influencer})
	public Proposal createProposal(@PathVariable Long campaignId,@RequestBody Proposal proposal
			,@RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		proposal = proposalService.createCampaignProposalByInfluencer(campaignId, proposal, this.getUserRequest(),locale);
		proposalService.processInboxPolling(proposal.getCampaign().getBrandId());
		proposalMessageService.processMessagePolling(proposal.getProposalId());
		return proposal;
	}
	@ApiOperation(value = "Get applied proposal in campaign")
	@RequestMapping(method=RequestMethod.GET,value="/{campaignId}/applied")
	@Authorization({Role.Influencer})
	public Proposal getAppliedProposal(@PathVariable Long campaignId) throws Exception {
		return proposalService.getAppliedProposal(this.getUserRequest().getInfluencer().getInfluencerId(),campaignId);
	}
}
