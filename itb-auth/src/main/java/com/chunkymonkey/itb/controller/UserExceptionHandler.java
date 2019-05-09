package com.chunkymonkey.itb.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.chunkymonkey.itb.domain.ItbUserError;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({AccessDeniedException.class})
	protected ResponseEntity<ItbUserError> handleAccessDenied(AccessDeniedException ex) {
		ItbUserError err = new ItbUserError();
		err.setMessage(ex.getMessage());
		err.setStatus(HttpStatus.UNAUTHORIZED);
		err.setDetail(ex.getCause().getMessage());
		return new ResponseEntity<ItbUserError>(err, err.getStatus());
	}
	
}
