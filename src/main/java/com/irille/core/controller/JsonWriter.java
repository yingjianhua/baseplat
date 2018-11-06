package com.irille.core.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonWriter {

	public static final String DEFAULT_CONTENT_TYPE = "application/json;charset=utf-8";

	private final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL).setLocale(null);

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	public ObjectMapper getPrettyObjectMapper() {
		return pretty;
	}
	
	public void setLocale(Locale locale) {
		this.objectMapper.setLocale(locale);
	}

	public void writeAsJson(Object o) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType(DEFAULT_CONTENT_TYPE);
		response.getWriter().print(objectMapper.writeValueAsString(o));
	}

	public static final ObjectMapper oMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);

	private static final ObjectMapper pretty = new ObjectMapper().setSerializationInclusion(Include.NON_NULL).enable(SerializationFeature.INDENT_OUTPUT);

	public static ObjectMapper defaultMapper() {
		return pretty;
	}

	public static void toConsole(Object o) {
		try {
			System.out.println(pretty.writeValueAsString(o));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public static String asString(Object o) throws JsonProcessingException {
		return pretty.writeValueAsString(o);
	}

	public static String asString4Log(String title, Object o) throws JsonProcessingException {
		return title + "]\r\n" + asString(o) + "\r\n";
	}

}
