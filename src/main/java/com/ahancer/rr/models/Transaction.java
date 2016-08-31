package com.ahancer.rr.models;

import java.io.Serializable;

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
import javax.persistence.SequenceGenerator;

import com.ahancer.rr.custom.type.TransactionStatus;

@Entity(name="transaction")
@SequenceGenerator(name="transactionSeq", initialValue=10000000, allocationSize=1)
public class Transaction extends AbstractModel implements Serializable {
	
	private static final long serialVersionUID = 7564704836479151058L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="transactionSeq")
	private Long transactionId;
	
	@Column(name="transactionNumber",length=6,unique=true,updatable=false)
	private String transactionNumber;
	
	@Column(name="userId", nullable = false)
	private Long userId;
	
	@MapsId("userId")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")
	private User user;
	
	@Column(name="amount",scale=10,precision=3)
	private Double amount;
	
	@Column(name="status",length=20)
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
	
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
	
}
