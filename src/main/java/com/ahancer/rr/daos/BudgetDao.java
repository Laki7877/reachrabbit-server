package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Budget;

public interface BudgetDao extends CrudRepository<Budget, Long> {

}
