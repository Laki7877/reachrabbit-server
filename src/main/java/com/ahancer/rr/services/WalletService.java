package com.ahancer.rr.services;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.models.Wallet;
import com.ahancer.rr.request.PayoutRequest;
import com.ahancer.rr.response.WalletAmount;

@Service
public interface WalletService {
	public Wallet findPendingByIndluencer(Long influencerId) throws Exception;
	public Transaction payoutWallet(PayoutRequest request, Long influencerId,Locale locale) throws Exception;
	public WalletAmount getWalletAmount(Long influencerId) throws Exception;
}
