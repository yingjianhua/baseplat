package com.irille.core.repository.orm;

public class ColumnFactory {

	public static ColumnBuilder type(ColumnTypes type) {
		return new ColumnBuilder(type);
	}
	
}
