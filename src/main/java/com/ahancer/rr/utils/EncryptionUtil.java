package com.ahancer.rr.utils;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {

	@Value("${reachrabbit.salt.workload}")
	private Integer workload;
	

	public String hashPassword(String password_plaintext) {
		String salt = BCrypt.gensalt(workload);
		String hashed_password = BCrypt.hashpw(password_plaintext, salt);
		return(hashed_password);
	}
	
	public boolean checkPassword(String password_plaintext, String stored_hash) {
		boolean password_verified = false;
		if(null == stored_hash || !stored_hash.startsWith("$2a$")) {
			return false;
		}
		password_verified = BCrypt.checkpw(password_plaintext, stored_hash);
		return password_verified;
	}
}
