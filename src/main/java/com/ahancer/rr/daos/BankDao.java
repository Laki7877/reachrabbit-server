package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Bank;

public interface BankDao extends CrudRepository<Bank, String> {
	
	public List<Bank> findAllByOrderByBankId();

}
