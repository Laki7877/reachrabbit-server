package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.CompletionTime;

@Repository
public interface CompletionTimeDao extends CrudRepository<CompletionTime, Long> {
	public List<CompletionTime> findAllByOrderByCompletionId();
}
