package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.Bank;

@Repository
public interface BankDao extends CrudRepository<Bank, String> {
	public List<Bank> findAllByIsActiveTrueOrderByBankId();
}
