package com.ahancer.rr.custom.type;

public enum WalletStatus {
	
	Pending("Pending"),
	Paid("Paid");
	
	private String displayName;

	WalletStatus(String displayName) {
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
