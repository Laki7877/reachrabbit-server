package com.ahancer.rr.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="bank")
public class Bank {

	@Id
	@Column(columnDefinition="CHAR(3)")
	private String bankId;
	
	@Column(name="createdBy",length=255)
	private String bankName;
	

	public Bank() {
		
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	
	
}
