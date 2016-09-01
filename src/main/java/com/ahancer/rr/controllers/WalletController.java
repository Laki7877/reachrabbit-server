package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Wallet;
import com.ahancer.rr.services.WalletService;

@RestController
@RequestMapping("/wallets")
public class WalletController extends AbstractController {
	
	@Autowired
	private WalletService walletService;
	
	@RequestMapping(method=RequestMethod.GET)
	@Authorization(Role.Influencer)
	public Wallet getPendingWallet() throws Exception {
		return walletService.findPendingByIndluencer(this.getUserRequest().getInfluencer().getInfluencerId());
	}

}
