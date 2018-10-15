package com.irille.core.web.exception;

import java.text.MessageFormat;

public class WebMessageException extends WebException {
	
	public interface IWebMessage {
		public String getKey();
		public String getValue();
		public ReturnCode getCode();
	}

	private static final long serialVersionUID = 1L;

	public WebMessageException(String message) {
		super(ReturnCode.failure, message);
	}
	
	public WebMessageException(ReturnCode code, String message) {
		super(code, message);
	}
	
	public WebMessageException(ReturnCode code, String message, Object... params) {
		super(code, new MessageFormat(message).format(params).toString());
	}
	
	public WebMessageException(IWebMessage message) {
		super(message.getCode(), message.getValue());
	}
	
	public WebMessageException(IWebMessage message, Object... params) {
		super(message.getCode(), new MessageFormat(message.getValue()).format(params).toString());
	}
	
}
