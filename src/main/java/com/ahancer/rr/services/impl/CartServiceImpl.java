package com.ahancer.rr.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.CartStatus;
import com.ahancer.rr.daos.CartDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Cart;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.services.CartService;

@Component
@Transactional(rollbackFor=Exception.class)
public class CartServiceImpl implements CartService {
	@Autowired
	private CartDao cartDao;
	@Autowired
	private ProposalDao proposalDao;
	
	public Cart getInCartByBrand(Long brandId) throws Exception {
		return cartDao.findByBrandIdAndStatus(brandId, CartStatus.Incart);
	}
	
	public Cart addProposalToCart(Long proposalId,Long brandId) throws Exception {
		Proposal proposal = proposalDao.findByProposalIdAndCampaignBrandId(proposalId, brandId);
		if(null == proposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		if(null != proposal.getCartId()){
			throw new ResponseException("error.proposal.already.in.cart");
		}
		Cart cart = cartDao.findByBrandIdAndStatus(brandId, CartStatus.Incart);
		if(null == cart){
			cart = new Cart();
			cart.setStatus(CartStatus.Incart);
			cart.setBrandId(brandId);
		}
		cart = cartDao.save(cart);
		proposal.setCartId(cart.getCartId());
		proposal = proposalDao.save(proposal);
		cart.getProposals().add(proposal);
		return cart;
	}
	
	public void deleteProposalToCart(Long proposalId,Long brandId) throws Exception{
		Proposal proposal = proposalDao.findByProposalIdAndCampaignBrandId(proposalId, brandId);
		if(null == proposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		proposal.setCartId(null);
		proposal = proposalDao.save(proposal);
	}

}
