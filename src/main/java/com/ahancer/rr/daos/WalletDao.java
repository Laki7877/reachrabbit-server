package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.custom.type.WalletStatus;
import com.ahancer.rr.models.Wallet;

public interface WalletDao extends CrudRepository<Wallet, Long> {
	
	public Wallet findByInfluencerIdAndStatus(Long influencerId,WalletStatus status);
	
}
