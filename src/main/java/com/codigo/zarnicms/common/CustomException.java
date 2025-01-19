package com.codigo.zarnicms.common;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomException extends Exception {
	private static final long serialVersionUID = -1368788450729629614L;
	
	private HttpStatus status;
	
	public CustomException(HttpStatus status, String message, Throwable e) {
		super(message, e);
		this.status = status;
	}
	
	public CustomException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}
}
