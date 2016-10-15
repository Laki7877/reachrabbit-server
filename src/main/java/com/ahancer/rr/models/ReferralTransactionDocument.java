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

import com.ahancer.rr.custom.type.DocumentType;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name="referralTransactionDocument")
public class ReferralTransactionDocument extends AbstractModel implements Serializable {

	private static final long serialVersionUID = -1314098206029226112L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long documentId;
	
	@Column(name="transactionId", nullable = false)
	private Long transactionId;
	
	@MapsId("transactionId")
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "transactionId")
	@JsonBackReference(value="transaction-referral")
	private Transaction transaction;
	
	@Column(name="referralId",nullable=false)
	private String referralId;

	@MapsId("referralId")
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE)
	@JoinColumn(name="referralId")
	private Referral referral;
	
	@Column(name="proposalId",nullable=false)
	private Long proposalId;

	@MapsId("proposalId")
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE)
	@JoinColumn(name="proposalId")
	private Proposal proposal;
	
	@Column(name="amount",scale=10,precision=3)
	private Double amount;
	
	@Column(name="type",length=20)
	@Enumerated(EnumType.STRING)
	private DocumentType type;

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
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

	public String getReferralId() {
		return referralId;
	}

	public void setReferralId(String referralId) {
		this.referralId = referralId;
	}

	public Referral getReferral() {
		return referral;
	}

	public void setReferral(Referral referral) {
		this.referral = referral;
	}

	public Long getProposalId() {
		return proposalId;
	}

	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}
	
}
