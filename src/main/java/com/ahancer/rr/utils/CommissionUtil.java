package com.ahancer.rr.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommissionUtil {
	@Value("${business.commission.rate}")
	private Double commissionRate;
	@Value("${business.commission.fee}")
	private Double commissionFee;
	
	public Double calculate(Double price) {
		return (1.0 - commissionRate) * price - commissionFee; 
	}
	public Double[] getVariables() {
		return new Double[] { commissionRate, commissionFee };
	}
}
