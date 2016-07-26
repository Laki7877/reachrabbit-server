package com.ahancer.rr.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ex.printStackTrace();
        ResponseException responseEx = (ex instanceof ResponseException) ? (ResponseException)ex : new ResponseException(ex.getMessage());
        return new ResponseEntity<>(responseEx.getResponseMessage(), responseEx.getStatusCode());
    }
}
