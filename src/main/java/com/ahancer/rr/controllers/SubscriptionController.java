package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.models.Subscription;
import com.ahancer.rr.services.SubscriptionService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {
	
	@Autowired
	private SubscriptionService subscriptionService;
	
	@ApiOperation(value = "Create new subscription")
	@RequestMapping(method=RequestMethod.POST)
	public Subscription createSubscription(@RequestBody Subscription subscription){
		subscription = subscriptionService.createSubscription(subscription);
		return subscription;
	}
}
