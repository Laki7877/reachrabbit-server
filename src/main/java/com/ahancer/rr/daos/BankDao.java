package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Bank;

public interface BankDao extends CrudRepository<Bank, Long> {

}
