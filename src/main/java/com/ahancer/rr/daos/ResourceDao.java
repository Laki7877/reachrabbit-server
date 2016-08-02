package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Resource;

public interface ResourceDao extends CrudRepository<Resource, Long>{
	
}
