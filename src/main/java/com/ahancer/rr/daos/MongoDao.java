package com.ahancer.rr.daos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ahancer.rr.models.MongoModel;

public interface MongoDao extends MongoRepository<MongoModel, String> {
	
}
