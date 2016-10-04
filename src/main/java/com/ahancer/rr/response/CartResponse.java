package com.ahancer.rr.response;

import java.io.Serializable;

import com.ahancer.rr.custom.type.CartStatus;
import com.ahancer.rr.models.Cart;

public class CartResponse implements Serializable {
	
	private static final long serialVersionUID = -5295181698299635620L;
	private Long cartId;
	private CartStatus status;
	
	public CartResponse(){
		
	}
	public CartResponse(Cart cart) {
		this.cartId = cart.getCartId();
		this.status = cart.getStatus();
	}
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
	public CartStatus getStatus() {
		return status;
	}
	public void setStatus(CartStatus status) {
		this.status = status;
	}
}
