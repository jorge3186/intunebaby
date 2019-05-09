package com.chunkymonkey.itb.domain;

import org.springframework.http.HttpStatus;

public class ItbBabyError {

	private HttpStatus status;
	
	private String message;
	
	private String detail;

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
