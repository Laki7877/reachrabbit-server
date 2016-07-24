package com.ahancer.rr.custum.type;

public enum CampaignStatus {
	Draft("Draft"),
	Open("Open"),
	WaitForPayment("Wait for Payment"),
	Production("Production"),
	Complete("Complete"),
	Cancel("Cancel");
	
	private String displayName;

	CampaignStatus(String displayName) {
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
