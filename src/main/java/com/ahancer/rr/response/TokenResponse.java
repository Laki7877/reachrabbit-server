package com.ahancer.rr.response;

import java.io.Serializable;
import java.util.Date;

public class TokenResponse implements Serializable {
	
	private static final long serialVersionUID = -1532976096496331837L;
	private Long userId;
	private String ip;
	private Date loginDt;
	public TokenResponse(){
		
	}
	public TokenResponse(Long userId, String ip, Date loginDt) {
		super();
		this.userId = userId;
		this.ip = ip;
		this.loginDt = loginDt;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getLoginDt() {
		return loginDt;
	}
	public void setLoginDt(Date loginDt) {
		this.loginDt = loginDt;
	}
}
