package com.vkviatkouski.sample.rest;

public class RestBusinessException extends RuntimeException {
	private String code;
	private String message;
	
	public RestBusinessException(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}
