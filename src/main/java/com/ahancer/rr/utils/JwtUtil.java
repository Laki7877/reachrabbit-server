package com.ahancer.rr.utils;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

@Component
public class JwtUtil {
	
	
	@Value("${reachrabbit.jwt.issuer}")
	private String issuer;

	@Value("${reachrabbit.jwt.expire.hour}")
	private Integer hour;
	
	@Value("${reachrabbit.jwt.expire.minute}")
	private Integer minute;
	
	@Value("${reachrabbit.jwt.expire.second}")
	private Integer second;
	
	private Key key = MacProvider.generateKey();
	
	public String generateToken(Long userId) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, hour);
		cal.add(Calendar.MINUTE, minute);
		cal.add(Calendar.SECOND, second);
		String token = Jwts.builder()
				  .setSubject(String.valueOf(userId))
				  .setIssuer(issuer)
				  .setIssuedAt(new Date())
				  .setExpiration(cal.getTime())
				  .signWith(SignatureAlgorithm.HS512, key)
				  .compact();
		return token;
	}
	
	public Long getUserId(String token) throws SignatureException{
		try {
		    String subject = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
		    return Long.parseLong(subject);
		} catch (SignatureException e) {
			throw e;
		}
	}

}
