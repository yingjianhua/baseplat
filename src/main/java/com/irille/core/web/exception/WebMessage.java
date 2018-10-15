package com.irille.core.web.exception;

import com.irille.core.web.exception.WebMessageException.IWebMessage;

public enum WebMessage implements IWebMessage{

	submit_faile("submit.faile", "提交失败", ReturnCode.service_unknow);
	
	private WebMessage(String key, String value, ReturnCode code) {
		this.key = key;
		this.value = value;
		this.code = code;
	}
	
	private String key;
	private String value;
	private ReturnCode code;
	
	public String getKey() {
		return key;
	}
	@Override
	public String getValue() {
		return value;
	}
	@Override
	public ReturnCode getCode() {
		return code;
	}
	
}
