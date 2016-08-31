package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.services.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController extends AbstractController {
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping(method=RequestMethod.POST)
	@Authorization(Role.Brand)
	public Transaction createTransaction() throws Exception {
		Transaction transaction = transactionService.createTransactionByBrand(this.getUserRequest().getBrand().getBrandId());
		return transaction;
	}
}
