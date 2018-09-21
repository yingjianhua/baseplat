package com.irille.core.web.exception;

import java.text.MessageFormat;

public class WebException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WebException() {
		super();
	}

	public WebException(Throwable cause, String message) {
		super(message, cause);
	}
	
	public WebException(Throwable cause, String message, Object... params) {
		this(cause, new MessageFormat(message).format(params).toString());
	}
	
	public WebException(String message) {
		super(message);
	}

	public WebException(String message, Object... params) {
		this(new MessageFormat(message).format(params).toString());
	}
	
	public WebException(Throwable cause) {
		super(cause);
	}

}
