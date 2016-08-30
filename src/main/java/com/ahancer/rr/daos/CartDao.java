package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.custom.type.CartStatus;
import com.ahancer.rr.models.Cart;

public interface CartDao extends CrudRepository<Cart, Long> {
	
	public Cart findByProposalsCampaignBrandIdAndStatus(Long brandId, CartStatus status);
	public Cart findByProposalsCampaignBrandId(Long brandId);
}
