package com.ahancer.rr.response;

import java.io.Serializable;

public class WalletAmount  implements Serializable {
	
	private static final long serialVersionUID = 5595484808807267655L;
	private Double amount;
	
	public WalletAmount(){
		
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
