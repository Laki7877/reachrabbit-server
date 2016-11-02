package com.ahancer.rr.services;

import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
	public Future<Boolean> send(String to,String subject, String body);
}
