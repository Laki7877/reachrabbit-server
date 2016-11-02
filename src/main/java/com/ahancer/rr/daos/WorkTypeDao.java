package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.WorkType;

@Repository
public interface WorkTypeDao extends CrudRepository<WorkType, Long> {
	public List<WorkType> findAllByIsActiveTrueOrderByWorkTypeId();
}
