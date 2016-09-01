package com.ahancer.rr.response;

import com.ahancer.rr.custom.type.WalletStatus;
import com.ahancer.rr.models.Wallet;

public class WalletResponse {
	
	private Long walletId;
	private WalletStatus status;
	
	public WalletResponse(){
		
	}
	
	public WalletResponse(Wallet wallet){
		this.walletId = wallet.getWalletId();
		this.status = wallet.getStatus();
	}

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public WalletStatus getStatus() {
		return status;
	}

	public void setStatus(WalletStatus status) {
		this.status = status;
	}
}
