package com.ahancer.rr.custom.type;

public enum PaymentStatus {
	Pending("Pending"),
	WaitForApprove("Wait for Approve"),
	Paid("Paid");
	
	private String displayName;

	PaymentStatus(String displayName) {
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
