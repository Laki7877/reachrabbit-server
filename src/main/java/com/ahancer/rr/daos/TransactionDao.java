package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Transaction;

public interface TransactionDao extends CrudRepository<Transaction, Long> {

}
