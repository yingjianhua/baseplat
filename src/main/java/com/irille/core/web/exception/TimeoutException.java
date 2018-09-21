package com.irille.core.web.exception;

import java.text.MessageFormat;

public class TimeoutException extends WebException {

	private static final long serialVersionUID = 1L;

	public TimeoutException() {
		super();
	}

	public TimeoutException(Throwable cause, String message) {
		super(message, cause);
	}
	
	public TimeoutException(Throwable cause, String message, Object... params) {
		this(cause, new MessageFormat(message).format(params).toString());
	}
	
	public TimeoutException(String message) {
		super(message);
	}

	public TimeoutException(String message, Object... params) {
		this(new MessageFormat(message).format(params).toString());
	}
	
	public TimeoutException(Throwable cause) {
		super(cause);
	}
	
}
