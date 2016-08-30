package com.ahancer.rr.custom.type;

public enum CartStatus {
	
	Incart("Incart"),
	Checkout("Checkout");
	
	private String displayName;

	CartStatus(String displayName) {
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
