package com.ahancer.rr.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Transaction;

public interface TransactionDao extends CrudRepository<Transaction, Long> {
	public Page<Transaction> findAll(Pageable pageable);
	public Transaction findByTransactionIdAndUserId(Long transactionId,Long userId);
	public Transaction findByBrandTransactionDocumentCartIdAndUserId(Long cartId, Long userId);
	public Page<Transaction> findByUserId(Long userId,Pageable pageable); 
	public Transaction findByBrandTransactionDocumentCartId(Long cartId);
}
