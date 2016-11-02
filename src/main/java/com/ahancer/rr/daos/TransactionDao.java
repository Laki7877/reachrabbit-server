package com.ahancer.rr.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.custom.type.TransactionType;
import com.ahancer.rr.models.Transaction;

@Repository
public interface TransactionDao extends CrudRepository<Transaction, Long> {
	public Page<Transaction> findByType(TransactionType type,Pageable pageable);
	public Transaction findByTransactionIdAndUserId(Long transactionId,Long userId);
	public Transaction findByBrandTransactionDocumentCartIdAndUserId(Long cartId, Long userId);
	public Page<Transaction> findByTypeAndUserId(TransactionType type,Long userId,Pageable pageable); 
	public Transaction findByBrandTransactionDocumentCartId(Long cartId);
	public Transaction findByInfluencerTransactionDocumentWalletId(Long walletId);
	public Transaction findByInfluencerTransactionDocumentWalletIdAndUserId(Long walletId, Long influencerId);
}
