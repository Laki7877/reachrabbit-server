package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.services.ProposalMessageService;

@RestController
@RequestMapping("/proposalmessages")
public class ProposalMessageController extends AbstractController {
	
	@Autowired
	private ProposalMessageService proposalMessageService;
	
	@RequestMapping(method=RequestMethod.POST)
	@Authorization(value={Role.Admin,Role.Brand,Role.Influencer})
	public ProposalMessage createProposalMessage(@RequestBody ProposalMessage message) throws Exception {
		message = proposalMessageService.createProposalMessage(message, this.getUserRequest());
		return message;
	}
}
