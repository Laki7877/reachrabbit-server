package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Cart;
import com.ahancer.rr.services.CartService;

@RestController
@RequestMapping("/carts")
public class CartController extends AbstractController {
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping(method=RequestMethod.GET)
	@Authorization(Role.Brand)
	public Cart getInCart() throws Exception {
		return cartService.getInCartByBrand(this.getUserRequest().getBrand().getBrandId());
	}

}
