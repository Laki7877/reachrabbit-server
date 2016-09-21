package com.ahancer.rr.controllers;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.custom.type.TransactionType;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Resource;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.services.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController extends AbstractController {
	@Autowired
	private TransactionService transactionService;
	@RequestMapping(method=RequestMethod.GET)
	@Authorization({Role.Admin, Role.Influencer, Role.Brand})
	public Page<Transaction> getAllTransaction(@RequestParam TransactionType type, Pageable pageable) throws Exception {
		Page<Transaction> response = null;
		switch(this.getUserRequest().getRole()){
		case Brand:
		case Influencer:
			response = transactionService.findAllByUserTransaction(type,this.getUserRequest().getUserId(), pageable);
			break;
		case Admin:
			response = transactionService.findAllTransactions(type,pageable);
			break;
		default:
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
		}
		return response;
	}
	@RequestMapping(method=RequestMethod.POST)
	@Authorization(Role.Brand)
	public Transaction createTransaction(@RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		Transaction transaction = transactionService.createTransactionByBrand(this.getUserRequest(),locale);
		return transaction;
	}
	@RequestMapping(value="/{transactionId}",method=RequestMethod.GET)
	@Authorization(Role.Brand)
	public Transaction getTransaction(@PathVariable Long transactionId) throws Exception {
		Transaction response = null;
		switch(this.getUserRequest().getRole()){
		case Brand:
			response = transactionService.findOneTransaction(transactionId,this.getUserRequest().getBrand().getBrandId());
			break;
		case Influencer:
			response = transactionService.findOneTransaction(transactionId,this.getUserRequest().getInfluencer().getInfluencerId());
			break;
		case Admin:
			response = transactionService.findOneTransactionByAdmin(transactionId);
			break;
		default:
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
		}
		return response;
	}
	@RequestMapping(value="/{transactionId}/confirm",method=RequestMethod.PUT)
	@Authorization(Role.Admin)
	public Transaction confirmTransaction(@PathVariable Long transactionId, Locale local) throws Exception {
		Transaction transaction = transactionService.confirmTransaction(transactionId,local);
		return transaction;
	}
	@RequestMapping(value="/{transactionId}/paid",method=RequestMethod.PUT)
	@Authorization(Role.Admin)
	public Transaction payTransaction(@PathVariable Long transactionId,@RequestBody(required=false) Resource resource
			, @RequestHeader(value="Accept-Language",required=false,defaultValue="th") Locale locale) throws Exception {
		Transaction transaction = transactionService.payTransaction(transactionId,resource,locale);
		return transaction;
	}
	
}
