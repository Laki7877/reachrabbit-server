package com.ahancer.rr.request;

import java.io.Serializable;

public class InfluencerCommissionRequest implements Serializable {

	private static final long serialVersionUID = 7220520420761655117L;
	
	private Double commission;

	public Double getCommission() {
		return commission;
	}
	
	public void setCommission(Double commission) {
		this.commission = commission;
	}
	
	

}
