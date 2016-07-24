package com.ahancer.rr.custum.type;

public enum SubmissionStatus {
	WaitForSubmission("Wait for Submission"),
	WaitForReview("Wait for Review"),
	NeedRevision("Need Revision"),
	WaitForPost("Wait for Post"),
	Posted("Posted");

	private String displayName;

	SubmissionStatus(String displayName) {
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
