package com.irille.core.web.exception;

import java.text.MessageFormat;

public class FieldValidateException extends WebException {
	private static final long serialVersionUID = 1L;

	public FieldValidateException() {
		super();
	}

	public FieldValidateException(Throwable cause, String message) {
		super(message, cause);
	}
	
	public FieldValidateException(Throwable cause, String message, Object... params) {
		this(cause, new MessageFormat(message).format(params).toString());
	}
	
	public FieldValidateException(String message) {
		super(message);
	}

	public FieldValidateException(String message, Object... params) {
		this(new MessageFormat(message).format(params).toString());
	}
	
	public FieldValidateException(Throwable cause) {
		super(cause);
	}
}
