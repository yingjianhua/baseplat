package com.irille.core.repository.orm;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Index {

	private String name = "";

	private Column[] columns;

	private boolean unique = false;

	public Index(String name, Column[] columns, boolean unique) {
		super();
		this.name = name;
		this.columns = columns;
		this.unique = unique;
	}

	public String name() {
		return name;
	}

	public Column[] columns() {
		return columns;
	}

	public boolean unique() {
		return unique;
	}

	public String createSql() {
		String sql = "";
		if (this.unique)
			sql += "UNIQUE ";
		return sql + "KEY " + name + " (" + Stream.of(columns).map(Column::columnName).collect(Collectors.joining(",")) + ")";
	}

}
