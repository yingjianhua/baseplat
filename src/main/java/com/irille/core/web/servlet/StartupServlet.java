package com.irille.core.web.servlet;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.irille.core.commons.annotation.Scanner;
import com.irille.core.repository.db.ConnectionManager;
import com.irille.core.web.config.Configuration;
import com.irille.core.web.config.Configuration.RunWithStartup;

public class StartupServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(StartupServlet.class);

	private static final Map<String, Object> config_cache = new ConcurrentHashMap<>();

	private static final Object findConfigInstance(String className) throws Exception {
		if (!config_cache.containsKey(className) || config_cache.get(className) == null) {
			try {
				config_cache.put(className, Class.forName(className).newInstance());
			} catch (Exception e) {
				log.error("can't init " + className, e);
				throw e;
			}
		}
		return config_cache.get(className);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			Scanner.initAllBean();
			Scanner.findClassByAnnotation(Configuration.class, "com.irille").forEach(clazz -> {
				List<Method> methods = Scanner.findMethodByAnnotation(RunWithStartup.class, clazz);
				methods.forEach(method -> {
					try {
						method.invoke(findConfigInstance(clazz.getName()));
					} catch (Exception e) {
						log.error("init error", e);
						System.exit(1);
					}
				});
			});
			ConnectionManager.commitConnection();
		} catch (SQLException e) {
			log.error("init error", e);
			System.exit(1);
		}
	}

}
