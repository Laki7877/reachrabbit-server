package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Cart;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.services.CartService;
import com.ahancer.rr.services.TransactionService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/carts")
public class CartController extends AbstractController {
	@Autowired
	private CartService cartService;
	@Autowired
	private TransactionService transactionService;
	@ApiOperation(value = "Get active cart")
	@RequestMapping(method=RequestMethod.GET)
	@Authorization({Role.Brand})
	public Cart getInCart() throws Exception {
		return cartService.getInCartByBrand(this.getUserRequest().getBrand().getBrandId());
	}
	@ApiOperation(value = "Get transaction from cart")
	@RequestMapping(value="/{cartId}/transaction",method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Admin})
	public Transaction getTransactionFromCart(@PathVariable Long cartId) throws Exception {
		Transaction response = null;
		switch(this.getUserRequest().getRole()){
		case Brand:
			response = transactionService.findOneTransactionFromCartByBrand(cartId,this.getUserRequest().getBrand().getBrandId());
			break;
		case Admin:
			response = transactionService.findOneTransactionFromCartByAdmin(cartId);
			break;
		default:
			throw new ResponseException(HttpStatus.METHOD_NOT_ALLOWED,"error.unauthorize");
		}
		return response;
	}
}
