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

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name="brandTransactionDocument")
public class BrandTransactionDocument extends AbstractModel implements Serializable {

	private static final long serialVersionUID = 7082382880406273964L;

	@Id
	@Column(name="transactionId",unique = true, nullable = false)
	private Long transactionId;

	@MapsId("transactionId")
	@OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "transactionId")
	@JsonBackReference(value="transaction-brand")
	private Transaction transaction;
	
	@Column(name="cartId",nullable=false)
	private Long cartId;

	@MapsId("cartId")
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE)
	@JoinColumn(name="cartId")
	private Cart cart;
	
	public BrandTransactionDocument() {
		
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

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

}
