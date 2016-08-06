package com.ahancer.rr.exception;

import org.springframework.http.HttpStatus;

public class ResponseException extends Exception {

	private static final long serialVersionUID = -4770214264061469688L;
	private HttpStatus statusCode;
	private String message;
	
	
	public ResponseException(HttpStatus statusCode, String message){
		this.statusCode = statusCode;
		this.message = message;
	}
	
	public ResponseException(){
		this(HttpStatus.INTERNAL_SERVER_ERROR,"error.internal.server"); 
	}
	
	public ResponseException(String message){
		this(HttpStatus.INTERNAL_SERVER_ERROR ,message);
	}
	
	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public Object getResponseMessage(){
		return new Object() {
			@SuppressWarnings("unused")
			public final String message = getMessage();
			@SuppressWarnings("unused")
			public final int statusCode = getStatusCode().value();
		};
	}

}
