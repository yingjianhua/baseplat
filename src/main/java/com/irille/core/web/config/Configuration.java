package com.irille.core.web.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {

	
	/**
	 * 启动时运行
	 * <p>必须要在<code>@Configuration</code>注解的类下面才会生效
	 * 
	 * @see Configuration
	 * @author Jianhua Ying
	 *
	 */
	@Target({ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface RunWithStartup {

	}
}
