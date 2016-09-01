package com.ahancer.rr.services;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.CartStatus;
import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.custom.type.TransactionStatus;
import com.ahancer.rr.custom.type.TransactionType;
import com.ahancer.rr.daos.BrandTransactionDocumentDao;
import com.ahancer.rr.daos.CartDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.TransactionDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.BrandTransactionDocument;
import com.ahancer.rr.models.Cart;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.utils.EncodeUtil;

@Service
@Transactional(rollbackFor=Exception.class)
public class TransactionService {
	
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private CartDao cartDao;
	
	@Autowired
	private ProposalDao proposalDao;
	
	@Autowired
	private BrandTransactionDocumentDao brandTransactionDocumentDao;
	
	public Transaction createTransactionByBrand(Long brandId) throws Exception {
		Cart cart = cartDao.findByBrandIdAndStatus(brandId, CartStatus.Incart);
		if(null == cart){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.cart.not.exist");
		}
		//create transaction
		Transaction transaction = new Transaction();
		Double sum = cart.getProposals().stream().mapToDouble(o -> o.getPrice()).sum();
		transaction.setAmount(sum);
		transaction.setStatus(TransactionStatus.Pending);
		transaction.setUserId(brandId);
		transaction.setType(TransactionType.Payin);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 5);
		transaction.setExpiredAt(cal.getTime());
		transaction = transactionDao.save(transaction);
		transaction.setTransactionNumber(EncodeUtil.encode(transaction.getTransactionId()));
		transaction = transactionDao.save(transaction);
		//create document
		BrandTransactionDocument document = new BrandTransactionDocument();
		document.setCartId(cart.getCartId());
		document.setTransactionId(transaction.getTransactionId());
		brandTransactionDocumentDao.save(document);
		//update cart status
		cart.setStatus(CartStatus.Checkout);
		cartDao.save(cart);
		
//		document.setCart(cart);
//		document.setTransaction(transaction);
//		transaction.setBrandTransactionDocument(document);
		return transaction;
	}
	
	public Page<Transaction> findAllByUserTransaction(Long userId,Pageable pageable) {
		return transactionDao.findByUserId(userId, pageable);
	}
	
	public Page<Transaction> findAllTransactions(Pageable pageable) {
		return transactionDao.findAll(pageable);
	}
	
	public Transaction findOneTransactionFromCartByAdmin(Long cartId){
		return transactionDao.findByBrandTransactionDocumentCartId(cartId);
	}
	
	
	public Transaction findOneTransactionFromCartByBrand(Long cartId,Long brandId){
		return transactionDao.findByBrandTransactionDocumentCartIdAndUserId(cartId, brandId);
	}
	
	public Transaction findOneTransaction(Long transactioId,Long brandId) throws Exception {
		return transactionDao.findByTransactionIdAndUserId(transactioId,brandId);
	}
	
	public Transaction confirmTransaction(Long transactioId) throws Exception {
		Transaction transaction = transactionDao.findOne(transactioId);
		if(null == transaction){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.transaction.not.exist");
		}
		if(!TransactionStatus.Pending.equals(transaction.getStatus())){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.transaction.invalid.status");
		}
		//update transaction status
		transaction.setStatus(TransactionStatus.Complete);
		transaction = transactionDao.save(transaction);
		
		//update proposal status
		Calendar cal = Calendar.getInstance();
		for(Proposal proposal : transaction.getBrandTransactionDocument().getCart().getProposals()){
			proposal.setStatus(ProposalStatus.Working);
			Integer days = proposal.getCompletionTime().getDay();
			cal.add(Calendar.DATE, days);
			proposal.setDueDate(cal.getTime());
			proposalDao.save(proposal);
		}
		
		
		return transaction;
	}
	
}
