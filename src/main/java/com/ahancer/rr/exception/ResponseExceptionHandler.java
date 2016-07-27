package com.ahancer.rr.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@PropertySource("messages.properties")
public class ResponseExceptionHandler  {

	@Autowired
	private Environment env;
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> processValidationError(Exception ex) {
		ResponseException responseEx = null;
		if(ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException exception = (MethodArgumentNotValidException)ex;
			BindingResult result = exception.getBindingResult();
			FieldError error = result.getFieldError();
			String msg = env.getProperty(error.getDefaultMessage());
			responseEx = new ResponseException(msg, HttpStatus.BAD_REQUEST);
		}else if(ex instanceof ResponseException) {
			responseEx = (ResponseException)ex;
			responseEx.setMessage(env.getProperty(responseEx.getMessage()));
		}else {
			responseEx = new ResponseException(ex.getMessage()); 
		}
		return new ResponseEntity<>(responseEx.getResponseMessage(), responseEx.getStatusCode());
	}
	
}
