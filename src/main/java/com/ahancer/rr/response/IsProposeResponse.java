package com.ahancer.rr.response;

import java.io.Serializable;

public class IsProposeResponse implements Serializable {
	private static final long serialVersionUID = 3470614541011693836L;
	private Boolean isPropose;
	public IsProposeResponse(){
	}
	public Boolean getIsPropose() {
		return isPropose;
	}
	public void setIsPropose(Boolean isPropose) {
		this.isPropose = isPropose;
	}
}
