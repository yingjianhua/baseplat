package com.irille.core.web.config;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import irille.pub.Log;
import irille.pub.svr.DbPool;

public class AppConfig {
	private static final Log LOG = new Log(AppConfig.class);
	
	public static final Boolean dev;
	public static final ObjectMapper objectMapper;

	static {
		String filepath = "appconfig.properties";
		try {
			InputStream inStream = AppConfig.class.getClassLoader().getResourceAsStream(filepath);
			Properties properties = new Properties();
			properties.load(inStream);
			
			dev = Boolean.valueOf(properties.getProperty("dev", "false"));
			
			objectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
		} catch (Exception e) {
			LOG.err("properties is not exists", "配置文件【{0}】不存在", filepath);
			throw new AssertionError();
		}
	}
	public static void db_connection_close() {
		DbPool.getInstance().removeConn();
	}
	public static void dbpool_release() {
		DbPool.getInstance().releaseAll();
	}
	public static void db_connection_commit() throws SQLException {
		DbPool.getInstance().getConn().commit();
	}
	public static void db_connection_rollback() throws SQLException {
		DbPool.getInstance().getConn().rollback();
	}
	
}
