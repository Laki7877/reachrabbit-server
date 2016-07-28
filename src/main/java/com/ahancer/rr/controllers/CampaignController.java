package com.ahancer.rr.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Campaign;

@RestController
@RequestMapping("/campaigns")
public class CampaignController extends AbstractController{
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Campaign> GetAllCampaign() throws Exception{
		throw new ResponseException();
	}
	
	@RequestMapping(value="/{campaignId}",method=RequestMethod.GET)
	public Campaign GetOneCampaign(@PathVariable Long campaignId) throws Exception{
		throw new ResponseException();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Campaign CreateCampaign(@RequestBody Campaign campaign) throws Exception{
		throw new ResponseException();
	}
	
	@RequestMapping(value="/{campaignId}",method=RequestMethod.PUT)
	public Campaign UpdateCampaign(@PathVariable Long campaignId,@RequestBody Campaign campaign) throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
	@RequestMapping(value="/{campaignId}",method=RequestMethod.DELETE)
	public void DeleteCampaign(@PathVariable Long campaignId) throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
}
