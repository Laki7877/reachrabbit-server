package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Budget;

public interface BudgetDao extends CrudRepository<Budget, Long> {
	
	public List<Budget> findAllByIsActiveTrueOrderByBudgetId();

}
