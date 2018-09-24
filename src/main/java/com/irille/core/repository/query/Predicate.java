package com.irille.core.repository.query;

import com.irille.core.repository.orm.Column;

public class Predicate {
	private Column column;
	private String alias;
	
	public Predicate(Column column) {
		this.column = column;
	}

	public Column column() {
		return column;
	}
	public String alias() {
		return alias;
	}
	public Predicate alias(String alias) {
		this.alias = alias;
		return this;
	}

}
