package com.ahancer.rr.exception;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.parboiled.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.response.UserResponse;

@ControllerAdvice
public class ResponseExceptionHandler  {
	
	private static final Logger logger = LoggerFactory.getLogger(ResponseExceptionHandler.class);

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private HttpServletRequest request;

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
			if(null != request){
				Object user = request.getAttribute(ApplicationConstant.UserRequest);
				if(user instanceof UserResponse){
					UserResponse userResponse = (UserResponse)user;
					MDC.put("User", userResponse.getEmail());
				}
				String browserName = request.getHeader(ApplicationConstant.UserAgentHeader);
				if(StringUtils.isNotEmpty(browserName)){
					 MDC.put("Browser", browserName);
				}
			}
			logger.error("Exception caught",ex);
			responseEx = new ResponseException(ex.getMessage()); 
		}
		return new ResponseEntity<>(responseEx.getResponseMessage(), responseEx.getStatusCode());
	}

}
