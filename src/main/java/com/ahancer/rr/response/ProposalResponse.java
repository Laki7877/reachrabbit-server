package com.ahancer.rr.response;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.CompletionTime;
import com.ahancer.rr.models.Media;
import com.ahancer.rr.models.Proposal;

public class ProposalResponse {
	
	private Long proposalId;
	private Long influencerId;
	private InfluencerResponse influencer;
	private CampaignResponse campaign;
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
	private WalletResponse wallet;
	private Boolean rabbitFlag;
	
	public ProposalResponse(){
		
	}
	
	public ProposalResponse(Proposal proposal, String roleValue){
		Role role = Role.valueOf(roleValue);
		switch (role) {
			case Admin:
				break;
			case Brand:
				break;
			case Influencer:
				this.rabbitFlag = proposal.getRabbitFlag();
				break;
			default:
				break;
		}
		this.proposalId = proposal.getProposalId();
		this.influencerId = proposal.getInfluencerId();
		this.influencer = new InfluencerResponse(proposal.getInfluencer(), roleValue);
		this.campaign = new CampaignResponse(proposal.getCampaign(), roleValue);
		this.completionTime = proposal.getCompletionTime();
		this.media = proposal.getMedia();
		this.price = proposal.getPrice();
		this.fee = proposal.getFee();
		this.status = proposal.getStatus();
		this.messageUpdatedAt = proposal.getMessageUpdatedAt();
		this.dueDate = proposal.getDueDate();
		this.completeDate = proposal.getCompleteDate();
		if(null != proposal.getCart()){
			this.setCartId(proposal.getCartId());
			this.setCart(new CartResponse(proposal.getCart()));
		}
		if(null != proposal.getWallet()){
			this.setWalletId(proposal.getWalletId());
			this.setWallet(new WalletResponse(proposal.getWallet()));
		}
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

	public InfluencerResponse getInfluencer() {
		return influencer;
	}

	public void setInfluencer(InfluencerResponse influencer) {
		this.influencer = influencer;
	}

	public CampaignResponse getCampaign() {
		return campaign;
	}

	public void setCampaign(CampaignResponse campaign) {
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

	public WalletResponse getWallet() {
		return wallet;
	}

	public void setWallet(WalletResponse wallet) {
		this.wallet = wallet;
	}

	public Boolean getRabbitFlag() {
		return rabbitFlag;
	}

	public void setRabbitFlag(Boolean rabbitFlag) {
		this.rabbitFlag = rabbitFlag;
	}
}
