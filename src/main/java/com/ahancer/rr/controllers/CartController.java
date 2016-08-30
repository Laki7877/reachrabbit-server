package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public Page<Cart> getAllCart(Pageable pageRequest) throws Exception {
		return cartService.getAllCartByBrand(this.getUserRequest().getBrand().getBrandId(), pageRequest);
		
	}
	

}
