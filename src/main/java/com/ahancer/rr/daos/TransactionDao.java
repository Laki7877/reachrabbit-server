package com.ahancer.rr.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.models.Transaction;

public interface TransactionDao extends CrudRepository<Transaction, Long> {
	public Page<Transaction> findAll(Pageable pageable);
	public Transaction findByTransactionIdAndUserId(@Param("transactionId") Long transactionId,@Param("userId") Long userId);
	public Transaction findByBrandTransactionDocumentCartIdAndUserId(Long cartId, Long userId);
}
