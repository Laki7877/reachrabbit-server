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
import javax.persistence.OneToOne;

import com.ahancer.rr.custom.type.PaymentStatus;

@Entity(name="payment")
public class Payment extends AbstractModel implements Serializable{
	
	private static final long serialVersionUID = 4988663340552083919L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long paymentId;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId",nullable=false)
	private User user;
	
	@Column(name="amount",scale=10,precision=3,nullable=false)
	private Double amount;
	
	@Column(name="status",length=20)
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;
	
	
	public Payment() {
		
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
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

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

}
