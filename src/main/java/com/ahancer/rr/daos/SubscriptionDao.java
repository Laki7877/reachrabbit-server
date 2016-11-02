package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.Subscription;

@Repository
public interface SubscriptionDao extends CrudRepository<Subscription, String>{

}
