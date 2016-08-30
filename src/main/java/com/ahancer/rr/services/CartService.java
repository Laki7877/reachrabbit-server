package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.CartStatus;
import com.ahancer.rr.daos.CartDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Cart;
import com.ahancer.rr.models.Proposal;

@Service
@Transactional(rollbackFor=Exception.class)
public class CartService {
	
	@Autowired
	private CartDao cartDao;
	
	@Autowired
	private ProposalDao proposalDao;
	
	public Cart getInCartByBrand(Long brandId){
		return cartDao.findByProposalsCampaignBrandId(brandId);
	}
	
	public Cart addProposalToCart(Long proposalId,Long brandId) throws Exception{
		Proposal proposal = proposalDao.findByProposalIdAndCampaignBrandId(proposalId, brandId);
		if(null == proposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		if(null != proposal.getCartId()){
			throw new ResponseException("error.proposal.already.in.cart");
		}
		Cart cart = cartDao.findByProposalsCampaignBrandIdAndStatus(brandId, CartStatus.Incart);
		if(null == cart){
			cart = new Cart();
			cart.setStatus(CartStatus.Incart);
		}
		cart = cartDao.save(cart);
		proposal.setCartId(cart.getCartId());
		proposal = proposalDao.save(proposal);
		return cart;
	}

}
