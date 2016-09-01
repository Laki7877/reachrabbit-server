package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;

import com.ahancer.rr.custom.type.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@JsonIdentityInfo(generator=JSOGGenerator.class)
@Entity(name="transaction")
@SequenceGenerator(name="transactionSeq", initialValue=10000000, allocationSize=1)
public class Transaction extends AbstractModel implements Serializable {
	
	private static final long serialVersionUID = 7564704836479151058L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="transactionSeq")
	private Long transactionId;
	
	@Column(name="transactionNumber",length=6)
	private String transactionNumber;
	
	@Column(name="userId", nullable = false)
	private Long userId;
	
	@JsonIgnore
	@MapsId("userId")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	private User user;
	
	@Column(name="amount",scale=10,precision=3)
	private Double amount;
	
	@Column(name="status",length=20)
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
	
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL,mappedBy="transaction")
	@PrimaryKeyJoinColumn
	@JsonManagedReference(value="transaction-brand")
	private BrandTransactionDocument brandTransactionDocument;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL,mappedBy="transaction")
	@PrimaryKeyJoinColumn
	@JsonBackReference(value="transaction-influencer")
	private InfluencerTransactionDocument influencerTransactionDocument;
	
	public Transaction(){
		
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public BrandTransactionDocument getBrandTransactionDocument() {
		return brandTransactionDocument;
	}

	public void setBrandTransactionDocument(BrandTransactionDocument brandTransactionDocument) {
		this.brandTransactionDocument = brandTransactionDocument;
	}

	public InfluencerTransactionDocument getInfluencerTransactionDocument() {
		return influencerTransactionDocument;
	}

	public void setInfluencerTransactionDocument(InfluencerTransactionDocument influencerTransactionDocument) {
		this.influencerTransactionDocument = influencerTransactionDocument;
	}
	
}
