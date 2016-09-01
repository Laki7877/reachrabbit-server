package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.WalletStatus;
import com.ahancer.rr.daos.WalletDao;
import com.ahancer.rr.models.Wallet;

@Service
@Transactional(rollbackFor=Exception.class)
public class WalletService {
	
	@Autowired
	private WalletDao walletDao;
	
	public Wallet findPendingByIndluencer(Long influencerId) {
		return walletDao.findByInfluencerIdAndStatus(influencerId, WalletStatus.Pending);
	}

}
