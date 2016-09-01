package com.ahancer.rr.request;

import com.ahancer.rr.models.Bank;

public class PayoutRequest {
	
	private Bank bank;
	private String accountNumber;
	private String accountName;
	
	public PayoutRequest(){
		
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}
