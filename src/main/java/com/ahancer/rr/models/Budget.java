package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="budget")
public class Budget implements Serializable {

	private static final long serialVersionUID = -2449020776236138591L;
	
	@Id
	@Column(name="budgetId",unique = true, nullable = false)
	private Long budgetId;
	
	@Column(name="fromBudget",scale=10,precision=3)
	private Double fromBudget;
	
	@Column(name="toBudget",scale=10,precision=3)
	private Double toBudget;
	
	@Column(name="isActive")
	private Boolean isActive;
	
	public Budget(){
		
	}

	public Long getBudgetId() {
		return budgetId;
	}

	public void setBudgetId(Long budgetId) {
		this.budgetId = budgetId;
	}

	public Double getFromBudget() {
		return fromBudget;
	}

	public void setFromBudget(Double fromBudget) {
		this.fromBudget = fromBudget;
	}

	public Double getToBudget() {
		return toBudget;
	}

	public void setToBudget(Double toBudget) {
		this.toBudget = toBudget;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
