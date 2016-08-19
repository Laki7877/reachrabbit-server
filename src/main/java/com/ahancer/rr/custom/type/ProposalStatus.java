package com.ahancer.rr.custom.type;

public enum ProposalStatus {
	
	Selection("Selection"),
	Working("Need Revision"),
	Complete("Reject");
	
	private String displayName;

	ProposalStatus(String displayName) {
		this.displayName = displayName;
	}

	public String displayName() { 
		return displayName; 
	}

	@Override 
	public String toString() { 
		return displayName; 
	}

}
