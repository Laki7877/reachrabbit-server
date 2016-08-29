package com.ahancer.rr.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.SubscriptionDao;
import com.ahancer.rr.models.Subscription;
import com.ahancer.rr.utils.EmailUtil;

@Service
@Transactional(rollbackFor=Exception.class)
public class SubscriptionService {
	
	@Autowired
	private SubscriptionDao subscriptionDao;
	
	
	private String email = "ahancercompany@gmail.com";
	private String passord = "Ahancer123!";
	
	public Subscription createSubscription(Subscription subscription){
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
		            + "\nEmail: "  + subscription.getEmail() 
		            + "\nSubscribed from http://www.reachrabbit.com";
		EmailUtil.sendFromGMail(email, passord, receipts , "New Subscription", body);
		return subscription;
	}

}
