package com.ahancer.rr.services;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=Exception.class)
public class FacebookService {
	@Value("${reachrabbit.app.facebook.id}")
	private String appId;
	
	@Value("${reachrabbit.app.facebook.secret}")
	private String appSecret;
	
}
