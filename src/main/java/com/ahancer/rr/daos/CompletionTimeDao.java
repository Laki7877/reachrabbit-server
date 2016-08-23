package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.CompletionTime;

public interface CompletionTimeDao extends CrudRepository<CompletionTime, Long> {
	
	public List<CompletionTime> findAllByOrderByCompletionId();

}
