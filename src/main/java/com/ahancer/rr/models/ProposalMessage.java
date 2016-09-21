package com.ahancer.rr.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity(name="proposalMessage")
public class ProposalMessage implements Serializable  {

	private static final long serialVersionUID = 2277636873977709793L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long messageId;
	
	@Column(name="proposalId", nullable = false)
	private Long proposalId;
	
	@JsonIgnore
	@MapsId("proposalId")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="proposalId",nullable=false)
	private Proposal proposal;
	
	@Column(name="userId", nullable = false)
	private Long userId;
	
	@MapsId("userId")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")
	private User user;
	
	@Column(name="message")
	@Lob
	private String message;
	
	@Column(name="isInfluencerRead")
	private Boolean isInfluencerRead;
	
	@Column(name="isBrandRead")
	private Boolean isBrandRead;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="proposalMessageResource",
			joinColumns=@JoinColumn(name="messageId", referencedColumnName="messageId"),
			inverseJoinColumns=@JoinColumn(name="resourceId", referencedColumnName="resourceId"))
	private Set<Resource> resources = new HashSet<Resource>(0);
	
	@Column(name="referenceId",length=64)
	private String referenceId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdAt",updatable=false,insertable=true)
	private Date createdAt;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedAt")
	private Date updatedAt;
	
	@JsonIgnore
	@Column(name="createdBy")
	private Long createdBy;
	
	@JsonIgnore
	@Column(name="updatedBy")
	private Long updatedBy;
	
	@PrePersist
	protected void onCreate() {
		createdAt = updatedAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = new Date();
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}
	
	public Boolean getIsInfluencerRead() {
		return isInfluencerRead;
	}

	public void setIsInfluencerRead(Boolean isInfluencerRead) {
		this.isInfluencerRead = isInfluencerRead;
	}

	public Boolean getIsBrandRead() {
		return isBrandRead;
	}

	public void setIsBrandRead(Boolean isBrandRead) {
		this.isBrandRead = isBrandRead;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Long getProposalId() {
		return proposalId;
	}

	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
	}
	
}
