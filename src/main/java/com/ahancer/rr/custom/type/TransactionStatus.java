package com.ahancer.rr.custom.type;

public enum TransactionStatus {
	Pending("Pending"),
	Complete("Complete");

	private String displayName;

	TransactionStatus(String displayName) {
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
