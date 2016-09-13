package com.ahancer.rr.response;

import java.io.Serializable;

public class ProposalCountResponse implements Serializable {

	private static final long serialVersionUID = -239695158904413892L;
	private Long proposalCount;
	private Long unreadMessageCount;
	
	public ProposalCountResponse() {
		
	}
	
	public ProposalCountResponse(Long proposalCount, Long unreadMessageCount) {
		this.proposalCount = proposalCount;
		this.unreadMessageCount = unreadMessageCount;
	}

	public Long getProposalCount() {
		return proposalCount;
	}

	public void setProposalCount(Long proposalCount) {
		this.proposalCount = proposalCount;
	}

	public Long getUnreadMessageCount() {
		return unreadMessageCount;
	}

	public void setUnreadMessageCount(Long unreadMessageCount) {
		this.unreadMessageCount = unreadMessageCount;
	}
	
	
}
