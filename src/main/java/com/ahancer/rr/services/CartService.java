package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	public Page<Cart> getAllCartByBrand(Long brandId, Pageable pageable){
		return cartDao.findByProposalsCampaignBrandId(brandId,pageable);
	}
	
	public Cart addProposalToCart(Long proposalId,Long brandId) throws Exception{
		Proposal proposal = proposalDao.findByProposalIdAndCampaignBrandId(proposalId, brandId);
		if(null == proposal){
			throw new ResponseException("error.proposal.not.exist");
		}
		if(null == proposal.getCartId() || 0 == proposal.getCartId()){
			throw new ResponseException("error.proposal.already.in.cart");
		}
		Cart cart = cartDao.findByProposalsCampaignBrandIdAndStatus(brandId, CartStatus.Incart);
		if(null == cart){
			cart = new Cart();
			cart.setStatus(CartStatus.Incart);
		}
		cart.getProposals().add(proposal);
		cart = cartDao.save(cart);
		return cart;
	}

}
