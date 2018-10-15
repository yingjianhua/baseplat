package com.irille.core.web.exception;

import java.text.MessageFormat;

public class TimeoutException extends WebException {

	private static final long serialVersionUID = 1L;

	public TimeoutException() {
		super();
	}
	
	public TimeoutException(String message) {
		super(message);
	}

	public TimeoutException(String message, Object... params) {
		this(new MessageFormat(message).format(params).toString());
	}
	
	@Override
	public ReturnCode getCode() {
		return ReturnCode.service_unknow;
	}
	
}
