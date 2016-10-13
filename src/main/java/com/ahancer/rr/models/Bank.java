package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="bank")
public class Bank implements Serializable{

	private static final long serialVersionUID = 4625248350260494348L;

	@Id
	@Column(columnDefinition="CHAR(3)")
	private String bankId;
	
	@Column(name="bankName",length=255)
	private String bankName;
	
	@JsonIgnore
	@Column(name="isActive")
	private Boolean isActive;

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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
}
