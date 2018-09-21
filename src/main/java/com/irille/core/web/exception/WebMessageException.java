package com.irille.core.web.exception;

import java.text.MessageFormat;

public class WebMessageException extends WebException {

	private static final long serialVersionUID = 1L;

	public WebMessageException() {
		super();
	}

	public WebMessageException(Throwable cause, String message) {
		super(message, cause);
	}
	
	public WebMessageException(Throwable cause, String message, Object... params) {
		this(cause, new MessageFormat(message).format(params).toString());
	}
	
	public WebMessageException(String message) {
		super(message);
	}

	public WebMessageException(String message, Object... params) {
		this(new MessageFormat(message).format(params).toString());
	}
	
	public WebMessageException(Throwable cause) {
		super(cause);
	}
}
