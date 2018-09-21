package com.irille.core.web.exception;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebExceptionMapper {

	private static final Map<Class<?>, Integer> map = new ConcurrentHashMap<>();
	
	static {
		register(WebException.class, 0);
		register(TimeoutException.class, -1);
		register(WebMessageException.class, -2);
		register(FieldValidateException.class, -3);
	}
	public static <T extends WebException> Integer getCode(Class<T> exceptionClass) {
		if(map.containsKey(exceptionClass))
			return map.get(exceptionClass);
		else
			return map.get(WebException.class);
	}
	private static <T extends WebException> void register(Class<T> exceptionClass, Integer code) {
		map.put(exceptionClass, code);
	}
}
