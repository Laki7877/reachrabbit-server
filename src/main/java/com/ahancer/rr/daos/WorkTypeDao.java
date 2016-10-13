package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.WorkType;

public interface WorkTypeDao extends CrudRepository<WorkType, Long> {
	public List<WorkType> findAllByIsActiveTrueOrderByWorkTypeId();
}
