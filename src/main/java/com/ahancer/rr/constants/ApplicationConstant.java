package com.ahancer.rr.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class ApplicationConstant {
	
	public final static String UserRequest = "UserRequest";
	public final static String TokenAttribute = "UserToken";
	public final static String UserAgentHeader = "User-Agent";
	public final static String TokenHeader = "X-Auth-Token";
	public final static String UserRequestCache = "UserRequestCache";
	public final static String TemporaryFolder = "temporary";
	public final static String DashboardRequestCache = "DashboardRequestCache";
	
	
	public static String Bucket;

    @Value("${cloud.aws.s3.bucket}")
    private void setBucket(String bucket) {
    	Bucket = bucket;
    }
	
	private ApplicationConstant() {
		
	}
}
