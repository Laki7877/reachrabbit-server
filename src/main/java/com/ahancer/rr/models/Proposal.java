package com.ahancer.rr.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.ahancer.rr.custom.type.ProposalStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity(name="proposal")
public class Proposal extends AbstractModel implements Serializable{
	
	private static final long serialVersionUID = 2295333503977165543L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long proposalId;
	
	@Column(name="influencerId", nullable = false)
	private Long influencerId;
	
	@MapsId("influencerId")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="influencerId")
	private Influencer influencer;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="campaignId",nullable=false)
	private Campaign campaign;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="completionId")
	private CompletionTime completionTime;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="proposalMedia",
			joinColumns=@JoinColumn(name="proposalId", referencedColumnName="proposalId"),
			inverseJoinColumns=@JoinColumn(name="mediaId", referencedColumnName="mediaId"))
	@OrderBy("mediaId")
	private Set<Media> media = new HashSet<Media>(0);
	
	@Transient
	@JsonSerialize
	@JsonDeserialize
	private String description;
	
	@Column(name="price",scale=10,precision=3)
	private Double price;
	
	@Column(name="fee",scale=10,precision=3)
	private Double fee;
	
	@Column(name="status",length=20)
	@Enumerated(EnumType.STRING)
	private ProposalStatus status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "messageUpdatedAt")
	private Date messageUpdatedAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dueDate")
	private Date dueDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "completeDate")
	private Date completeDate;
	
	@Column(name="cartId")
	private Long cartId;
	
	@JsonIgnore
	@MapsId("cartId")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cartId")
	private Cart cart;
	
	@Column(name="walletId")
	private Long walletId;
	
	@JsonIgnore
	@MapsId("walletId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "walletId")
	private Wallet wallet;
	
	
	public Proposal() {
		
	}

	public Long getProposalId() {
		return proposalId;
	}

	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
	}

	public Influencer getInfluencer() {
		return influencer;
	}

	public void setInfluencer(Influencer influencer) {
		this.influencer = influencer;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public CompletionTime getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(CompletionTime completionTime) {
		this.completionTime = completionTime;
	}

	public Set<Media> getMedia() {
		return media;
	}

	public void setMedia(Set<Media> media) {
		this.media = media;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public ProposalStatus getStatus() {
		return status;
	}

	public void setStatus(ProposalStatus status) {
		this.status = status;
	}

	public Date getMessageUpdatedAt() {
		return messageUpdatedAt;
	}

	public void setMessageUpdatedAt(Date messageUpdatedAt) {
		this.messageUpdatedAt = messageUpdatedAt;
	}

	public Long getInfluencerId() {
		return influencerId;
	}

	public void setInfluencerId(Long influencerId) {
		this.influencerId = influencerId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
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
