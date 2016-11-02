package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.Budget;

@Repository
public interface BudgetDao extends CrudRepository<Budget, Long> {
	public List<Budget> findAllByIsActiveTrueOrderByBudgetId();
}
