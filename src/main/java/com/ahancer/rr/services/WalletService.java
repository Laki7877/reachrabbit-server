package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.WalletStatus;
import com.ahancer.rr.daos.WalletDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.InfluencerTransactionDocument;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.models.Wallet;
import com.ahancer.rr.request.PayoutRequest;

@Service
@Transactional(rollbackFor=Exception.class)
public class WalletService {
	
	@Autowired
	private WalletDao walletDao;
	
	public Wallet findPendingByIndluencer(Long influencerId) {
		return walletDao.findByInfluencerIdAndStatus(influencerId, WalletStatus.Pending);
	}
	
	public Transaction payoutWallet(PayoutRequest request, Long influencerId) throws Exception{
		
		Wallet wallet = walletDao.findByInfluencerIdAndStatus(influencerId, WalletStatus.Pending);
		if(null == wallet){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.wallet.not.exist");
		}
		wallet.setStatus(WalletStatus.Paid);
		wallet = walletDao.save(wallet);
		
		//Transaction 
		
		//create document
		InfluencerTransactionDocument document = new InfluencerTransactionDocument();
		document.setAccountName(request.getAccountName());
		document.setAccountNumber(request.getAccountNumber());
		document.setBank(request.getBank());
		document.setWalletId(wallet.getWalletId());
		
		
		return null;
	}

}
