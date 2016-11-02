package com.ahancer.rr.services;

import org.springframework.stereotype.Service;

import com.ahancer.rr.models.Subscription;

@Service
public interface SubscriptionService {
	public Subscription createSubscription(Subscription subscription) throws Exception;
}
