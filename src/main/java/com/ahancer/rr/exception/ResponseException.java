package com.ahancer.rr.exception;

import org.springframework.http.HttpStatus;

public class ResponseException extends Exception {

	private static final long serialVersionUID = -4770214264061469688L;
	private HttpStatus statusCode;
	private String message;
	
	
	public ResponseException(String message, HttpStatus statusCode){
		this.statusCode = statusCode;
		this.message = message;
	}
	
	public ResponseException(){
		this("error.internal.server",HttpStatus.INTERNAL_SERVER_ERROR); 
	}
	
	public ResponseException(String message){
		this(message ,HttpStatus.INTERNAL_SERVER_ERROR);
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
