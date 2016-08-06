package com.ahancer.rr.exception;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;

@ControllerAdvice
public class ResponseExceptionHandler  {

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> processValidationError(Exception ex, @RequestHeader("Accept-Language") Locale locale) {
		ex.printStackTrace();
		ResponseException responseEx = null;
		if(ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException exception = (MethodArgumentNotValidException)ex;
			BindingResult result = exception.getBindingResult();
			FieldError error = result.getFieldError();
			String msg = messageSource.getMessage(error.getDefaultMessage(),null,locale);
			responseEx = new ResponseException(HttpStatus.BAD_REQUEST, msg);
		}else if(ex instanceof ResponseException) {
			responseEx = (ResponseException)ex;
			String msg = messageSource.getMessage(responseEx.getMessage(),null,locale);
			responseEx.setMessage(msg);
		}else {
			responseEx = new ResponseException(ex.getMessage()); 
		}
		return new ResponseEntity<>(responseEx.getResponseMessage(), responseEx.getStatusCode());
	}

}
