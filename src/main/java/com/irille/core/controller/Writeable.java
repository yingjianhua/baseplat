package com.irille.core.controller;

import java.io.IOException;
import java.util.Collection;

import irille.view.BaseView;

public interface Writeable {
	
	public static final byte RET_CODE_SUCCESS = 1;//成功
	public static final byte RET_CODE_FAiLURE = 0;//失败
	public static final byte RET_CODE_TIMEOUT = -1;//失败
	
	/**
	 * 登录超时或者未登录
	 * @throws IOException
	 */
	default void writeTimeout() throws IOException {
		writeAsJson(RET_CODE_TIMEOUT, null, null);
	}
	default void writeErr(int code, String msg) throws IOException {
		writeAsJson(code, msg, null);
	}
	default void writeErr(String msg) throws IOException {
		writeAsJson(RET_CODE_FAiLURE, msg, null);
	}
	default void write() throws IOException {
		writeAsJson(RET_CODE_SUCCESS, null, null);
	}
	default <T extends BaseView> void write(T view) throws IOException {
		writeAsJson(RET_CODE_SUCCESS, null, view);
	}
	default <T extends BaseView> void write(Collection<T> list) throws IOException {
		writeAsJson(RET_CODE_SUCCESS, null, list);
	}
	default <T extends BaseView> void write(irille.view.Page<T> page) throws IOException {
		writeAsJson(RET_CODE_SUCCESS, null, page);
	}
	public static void writeAsJson(int ret, String msg, Object o) throws IOException {
		Result result = new Result();
		result.setRet(ret);
		result.setMsg(msg);
		result.setResult(o);
		JsonWriter.writeAsJson(result);
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
