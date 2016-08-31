package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.CartStatus;
import com.ahancer.rr.custom.type.TransactionStatus;
import com.ahancer.rr.daos.BrandTransactionDocumentDao;
import com.ahancer.rr.daos.CartDao;
import com.ahancer.rr.daos.TransactionDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.BrandTransactionDocument;
import com.ahancer.rr.models.Cart;
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
	
	public Transaction findOneTransaction(Long transactioId,Long brandId) throws Exception {
		return transactionDao.findByTransactionIdAndUserId(transactioId,brandId);
	}
	
}
