package com.ahancer.rr.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.SubscriptionDao;
import com.ahancer.rr.models.Subscription;
import com.ahancer.rr.services.EmailService;
import com.ahancer.rr.services.SubscriptionService;

@Component
@Transactional(rollbackFor=Exception.class)
public class SubscriptionServiceImpl implements SubscriptionService {
	
	@Autowired
	private SubscriptionDao subscriptionDao;
	
	@Autowired
	private EmailService emailService;
	
	public Subscription createSubscription(Subscription subscription) throws Exception{
		if(StringUtils.isEmpty(subscription.getEmail())){
			return subscription;
		}
		Subscription exist = subscriptionDao.findOne(subscription.getEmail());
		if(null != exist){
			return exist;
		}
		subscription = subscriptionDao.save(subscription);
		String[] receipts = { "plawooth.c@gmail.com","nattphenjati@gmail.com","jdumnern@gmail.com","laki7877@gmail.com" };
		String body = "Name: " + subscription.getName() 
		            + "<br>Email: "  + subscription.getEmail() 
		            + "<br>Subscribed from http://www.reachrabbit.com";
		String subject = "New Subscription";
		for(String to : receipts){
			emailService.send(to, subject, body);
		}
		
		return subscription;
	}

}
