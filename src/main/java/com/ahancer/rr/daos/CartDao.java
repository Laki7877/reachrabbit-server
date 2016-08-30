package com.ahancer.rr.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.custom.type.CartStatus;
import com.ahancer.rr.models.Cart;

public interface CartDao extends CrudRepository<Cart, Long> {
	
	public Cart findByProposalsCampaignBrandIdAndStatus(Long brandId, CartStatus status);
	public Page<Cart> findByProposalsCampaignBrandId(Long brandId, Pageable pageable);
}
