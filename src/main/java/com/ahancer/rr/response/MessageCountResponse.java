package com.ahancer.rr.response;

public class MessageCountResponse {
	
	private String email;
	private Long messageCount;
	
	public MessageCountResponse() {
		
	}
	public MessageCountResponse(String email, Long messageCount) {
		super();
		this.email = email;
		this.messageCount = messageCount;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(Long messageCount) {
		this.messageCount = messageCount;
	}
}
