package com.ahancer.rr.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ahancer.rr.models.Campaign;

@Controller
@RequestMapping("/campaigns")
public class CampaignController {
	
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> CreateCampaign(@RequestBody Campaign campaign) {
		
		return ResponseEntity.ok(""); 
	}
	
	

}
