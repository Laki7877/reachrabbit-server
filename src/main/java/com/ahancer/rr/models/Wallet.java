package com.ahancer.rr.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.OneToMany;

import com.ahancer.rr.custom.type.WalletStatus;

@Entity(name="wallet")
public class Wallet extends AbstractModel implements Serializable {
	
	private static final long serialVersionUID = -3826017683363504478L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long walletId;
	
	@Column(name="influencerId",nullable=false)
	private Long influencerId;

	@MapsId("influencerId")
	@ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.MERGE)
	@JoinColumn(name="influencerId")
	private Influencer influencer;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "cart",cascade=CascadeType.ALL)
	private Set<Proposal> proposals = new HashSet<Proposal>(0);
	
	@Column(name="status", length=20)
	@Enumerated(EnumType.STRING)
	private WalletStatus status;
	
	public Wallet() {
		
	}

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
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

	public Set<Proposal> getProposals() {
		return proposals;
	}

	public void setProposals(Set<Proposal> proposals) {
		this.proposals = proposals;
	}

	public WalletStatus getStatus() {
		return status;
	}

	public void setStatus(WalletStatus status) {
		this.status = status;
	}
	

}
