package com.irille.core.web.exception;

import java.text.MessageFormat;

public class WebException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private ReturnCode code;

	public WebException() {
		super();
	}
	
	public WebException(String message) {
		super(message);
		code = ReturnCode.tools_unknow;
	}
	
	public WebException(ReturnCode code, String message) {
		super(message);
		this.code = code;
	}

	public WebException(String message, Object... params) {
		this(new MessageFormat(message).format(params).toString());
	}
	
	public WebException(ReturnCode code, String message, Object... params) {
		this(code, new MessageFormat(message).format(params).toString());
	}
	
	public ReturnCode getCode() {
		return code;
	}
	
}
