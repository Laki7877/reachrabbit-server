package com.ahancer.rr.services;

import org.springframework.stereotype.Service;

import com.ahancer.rr.models.Cart;

@Service
public interface CartService {
	public Cart getInCartByBrand(Long brandId) throws Exception;
	public Cart addProposalToCart(Long proposalId,Long brandId) throws Exception;
	public void deleteProposalToCart(Long proposalId,Long brandId) throws Exception;

}
