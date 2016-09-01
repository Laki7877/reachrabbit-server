package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@JsonIdentityInfo(generator=JSOGGenerator.class)
@Entity(name="influencerTransactionDocument")
public class InfluencerTransactionDocument extends AbstractModel implements Serializable {

	private static final long serialVersionUID = -8423456738305930567L;

	@Id
	@Column(name="transactionId",unique = true, nullable = false)
	private Long transactionId;

	@MapsId("transactionId")
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "transactionId")
	@JsonManagedReference(value="transaction-influencer")
	private Transaction transaction;
	
	@Column(name="walletId",nullable=false)
	private Long walletId;

	@MapsId("walletId")
	@ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.MERGE)
	@JoinColumn(name="walletId")
	private Wallet wallet;
	
	public InfluencerTransactionDocument(){
		
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	
}
