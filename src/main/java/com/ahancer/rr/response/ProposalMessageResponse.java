package com.ahancer.rr.response;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.models.Resource;
import com.ahancer.rr.utils.Util;

public class ProposalMessageResponse implements Serializable {

	private static final long serialVersionUID = 8283523430188213776L;
	private Long messageId;
	private UserResponse user;
	private String message;
	private Boolean isInfluencerRead;
	private Boolean isBrandRead;
	private Set<Resource> resources = new HashSet<Resource>(0);
	private String referenceId;
	private Date createdAt;
	
	public ProposalMessageResponse(ProposalMessage message, String roleValue) {
		this.messageId = message.getMessageId();
		this.message = message.getMessage();
		this.isBrandRead = message.getIsBrandRead();
		this.isInfluencerRead = message.getIsInfluencerRead();
		this.resources = message.getResources();
		this.referenceId = message.getReferenceId();
		this.createdAt = message.getCreatedAt();

		this.user = Util.getUserResponse(message.getUser());
	}
	
	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public UserResponse getUser() {
		return user;
	}

	public void setUser(UserResponse user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
}
