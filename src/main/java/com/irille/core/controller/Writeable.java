package com.irille.core.controller;

import java.io.IOException;
import java.util.Collection;

import com.irille.core.web.exception.ReturnCode;
import com.irille.core.web.exception.WebMessageException.IWebMessage;

import irille.view.BaseView;
import irille.view.Page;

public interface Writeable {

	/**
	 * 登录超时或者未登录
	 * 
	 * @throws IOException
	 */
	default void writeTimeout() throws IOException {
		writeAsJson(ReturnCode.service_unknow, null, null);
	}

	default void writeErr(int code, String msg) throws IOException {
		writeAsJson(code, msg, null);
	}

	default void writeErr(String msg) throws IOException {
		writeAsJson(ReturnCode.third_unknow, msg, null);
	}

	default void write(ReturnCode code) throws IOException {
		writeAsJson(code, null, null);
	}

	default void write(ReturnCode code, String msg) throws IOException {
		writeAsJson(code, msg, null);
	}

	default void write(IWebMessage message) throws IOException {
		writeAsJson(message.getCode(), message.getValue(), null);
	}

	default void write() throws IOException {
		writeAsJson(ReturnCode.success, null, null);
	}

	default <T extends BaseView> void write(T view) throws IOException {
		writeAsJson(ReturnCode.success, null, view);
	}

	default <T extends BaseView> void write(Collection<T> list) throws IOException {
		writeAsJson(ReturnCode.success, null, list);
	}

	default <T extends BaseView> void write(Page<T> page) throws IOException {
		writeAsJson(ReturnCode.success, null, page);
	}

	default void writeAsJson(ReturnCode code, String msg, Object o) throws IOException {
		writeAsJson(code.getCode(), msg, o);
	}

	default void writeAsJson(int ret, String msg, Object o) throws IOException {
		Result result = new Result();
		result.setRet(ret);
		result.setMsg(msg);
		result.setResult(o);
		getWriter().writeAsJson(result);
	}

	public static final JsonWriter writer = new JsonWriter();

	default JsonWriter getWriter() {
		return writer;
	}

	public class Result {

		private int ret;
		private String msg;
		private Object result;

		public int getRet() {
			return ret;
		}

		public void setRet(int ret) {
			this.ret = ret;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public Object getResult() {
			return result;
		}

		public void setResult(Object result) {
			this.result = result;
		}
	}

}
