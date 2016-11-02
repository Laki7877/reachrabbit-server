package com.ahancer.rr.services;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ahancer.rr.custom.type.TransactionType;
import com.ahancer.rr.models.Resource;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.response.UserResponse;

@Service
public interface TransactionService {
	public Transaction createTransactionByBrand(UserResponse user,Locale locale) throws Exception;
	public Page<Transaction> findAllByUserTransaction(TransactionType type,Long userId,Pageable pageable) throws Exception;
	public Page<Transaction> findAllTransactions(TransactionType type,Pageable pageable) throws Exception;
	public Transaction findOneTransactionFromCartByAdmin(Long cartId) throws Exception;
	public Transaction findOneTransactionFromWalletByAdmin(Long walletId) throws Exception;
	public Transaction findOneTransactionFromWalletByInfluencer(Long walletId,Long influencerId) throws Exception;
	public Transaction findOneTransactionFromCartByBrand(Long cartId,Long brandId) throws Exception;
	public Transaction findOneTransaction(Long transactionId,Long uerId) throws Exception;
	public Transaction findOneTransactionByAdmin(Long transactionId) throws Exception;
	public Transaction confirmTransaction(Long transactioId, Locale locale) throws Exception;
	public Transaction payTransaction(Long transactioId,  Resource resource, Locale locale) throws Exception;
}
