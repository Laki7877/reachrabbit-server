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
import javax.persistence.OneToMany;

import com.ahancer.rr.custom.type.CartStatus;

@Entity(name="cart")
public class Cart extends AbstractModel implements Serializable {
	
	private static final long serialVersionUID = 8327636424184830607L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cartId;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "cart",cascade=CascadeType.ALL)
	private Set<Proposal> proposals = new HashSet<Proposal>(0);
	
	@Column(name="status", length=20)
	@Enumerated(EnumType.STRING)
	private CartStatus status;
	
	public Cart() {
		
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Set<Proposal> getProposals() {
		return proposals;
	}

	public void setProposals(Set<Proposal> proposals) {
		this.proposals = proposals;
	}

	public CartStatus getStatus() {
		return status;
	}

	public void setStatus(CartStatus status) {
		this.status = status;
	}

}
