package com.irille.core.repository.query;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.IColumnField;

public class Predicate implements IColumnField {
	private Column column;
	private String alias;
	
	public Predicate(Column column) {
		this.column = column;
	}
	@Override
	public String name() {
		return this.column().columnName();
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
	public Predicate as(String alias) {
		return new Predicate(column()).alias(alias);
	}
	public String columnName() {
    	return this.column().columnName();
    }
	public String columnFullName() {
    	return this.column().columnFullName();
    }
	public String columnNameWithAlias() {
		if(alias!=null)
			return this.columnFullName()+" as "+alias;
		else
			return this.column().columnNameWithAlias();
    }

}
