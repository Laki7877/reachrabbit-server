package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Subscription;

public interface SubscriptionDao extends CrudRepository<Subscription, String>{

}
