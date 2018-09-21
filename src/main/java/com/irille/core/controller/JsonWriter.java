package com.irille.core.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonWriter {
	
	public static final String DEFAULT_CONTENT_TYPE = "application/json;charset=utf-8";
	
	public static final ObjectMapper oMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
	
	public static void writeAsJson(Object o) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType(DEFAULT_CONTENT_TYPE);
		response.getWriter().print(oMapper.writeValueAsString(o));
	}
	public static void toConsole(Object o) {
		try {
			System.out.println(oMapper.writeValueAsString(o));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
}
