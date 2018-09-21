package com.irille.core.repository.orm;

import java.sql.SQLException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.irille.core.repository.db.ConnectionManager;

public abstract class Entity {
	
	private static final Logger logger = LoggerFactory.getLogger(Entity.class);
	
	public Entity init() {return this;};

	@Test
	public void testCreateTableIfNotExists() {
		try {
			Table<?> table = (Table<?>)this.getClass().getField("table").get(null);
			table.drop(false);
			table.create();
			ConnectionManager.commitConnection();
			new EntitySrc(this).outSrc();
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Table<?> table() {
		try {
			return (Table<?>)this.getClass().getField("table").get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			logger.error("实体[{}]没有table属性", this.getClass().getName());
			return null;
		}
	}
}
