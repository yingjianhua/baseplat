package com.irille.core.repository.orm;

public class TableFactory {
	
	public static <T extends Entity> TableBuilder<T> entity(Class<T> entityClass) {
		return new TableBuilder<>(entityClass);
	}
	
}
