package com.ahancer.rr.custum.type;

public enum Gender {
	Male("Male"),
	Female("Female"),
	NotSpecified("Not Specified");
	
	private String displayName;

	Gender(String displayName) {
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
