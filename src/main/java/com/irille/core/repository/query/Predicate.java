package com.irille.core.repository.query;

import java.io.Serializable;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.IColumnField;

public class Predicate implements IColumnField {
	private Column column;
	private String alias;
	private Serializable[] params;
	private String conditions;
	
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
	public Predicate eq(Serializable param) {
		this.conditions = "=?";
		this.params = new Serializable[] {param};
		return this;
	}
	public Predicate gt(Serializable param) {
		this.conditions = ">?";
		this.params = new Serializable[] {param};
		return this;
	}
	public Predicate lt(Serializable param) {
		this.conditions = "<?";
		this.params = new Serializable[] {param};
		return this;
	}
	public Predicate between(Serializable param, Serializable param2) {
		this.conditions = "between ? and ?";
		this.params = new Serializable[] {param, param2};
		return this;
	}
	public Predicate like(Serializable param) {
		this.conditions = "like ?";
		this.params = new Serializable[] {param};
		return this;
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
	public String getConditions() {
		return this.conditions;
	}
	public Serializable[] getParams() {
		return this.params;
	}

}
