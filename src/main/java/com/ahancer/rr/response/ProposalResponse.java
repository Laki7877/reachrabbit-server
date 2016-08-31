package com.ahancer.rr.response;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.CompletionTime;
import com.ahancer.rr.models.Influencer;
import com.ahancer.rr.models.Media;
import com.ahancer.rr.models.Wallet;

public class ProposalResponse {
	
	private Long proposalId;
	private Long influencerId;
	private Influencer influencer;
	private Campaign campaign;
	private CompletionTime completionTime;
	private Set<Media> media = new HashSet<Media>(0);
	private Double price;
	private Double fee;
	private ProposalStatus status;
	private Date messageUpdatedAt;
	private Date dueDate;
	private Date completeDate;
	private Long cartId;
	private CartResponse cart;
	private Long walletId;
	private Wallet wallet;
	
	public ProposalResponse(){
		
	}

	public Long getProposalId() {
		return proposalId;
	}

	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
	}

	public Long getInfluencerId() {
		return influencerId;
	}

	public void setInfluencerId(Long influencerId) {
		this.influencerId = influencerId;
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

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public CartResponse getCart() {
		return cart;
	}

	public void setCart(CartResponse cart) {
		this.cart = cart;
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
