package com.irille.core.repository.query;

import java.io.Serializable;

import com.irille.core.repository.orm.Column;

public class Predicate implements IPredicate {
	
	private Column column;
	private String alias = "";
	private Serializable[] params;
	private String conditions;
	
	public Predicate(Column column) {
		this.column = column;
		this.alias = column.fieldName();
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
	public Predicate ne(Serializable param) {
		this.conditions = "<>?";
		this.params = new Serializable[] {param};
		return this;
	}
	public Predicate gt(Serializable param) {
		this.conditions = ">?";
		this.params = new Serializable[] {param};
		return this;
	}
	public Predicate ge(Serializable param) {
		this.conditions = ">=?";
		this.params = new Serializable[] {param};
		return this;
	}
	public Predicate lt(Serializable param) {
		this.conditions = "<?";
		this.params = new Serializable[] {param};
		return this;
	}
	public Predicate le(Serializable param) {
		this.conditions = "<=?";
		this.params = new Serializable[] {param};
		return this;
	}
	public Predicate isNull() {
		this.conditions = "is null";
		return this;
	}
	public Predicate notNull() {
		this.conditions = "is not null";
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
	public Predicate desc() {
		this.conditions = "desc";
		return this;
	}
	public Predicate asc() {
		this.conditions = "asc";
		return this;
	}
	public Predicate params(Serializable... params) {
		this.params = params;
		return this;
	}
	public Predicate value(Serializable param) {
		this.params = new Serializable[] {param};
		return this;
	}
	@Override
	public String columnName() {
    	return this.column.columnName();
    }
	@Override
	public String columnFullName() {
    	return this.column.columnFullName();
    }
	@Override
	public String columnNameWithAlias() {
		if(alias!=null)
			return this.columnFullName()+" as "+alias;
		else
			return this.column.columnNameWithAlias();
    }
	@Override
	public String alias() {
		return this.alias;
	}
	@Override
	public String getConditions() {
		return this.conditions;
	}
	@Override
	public Serializable[] getParams() {
		return this.params;
	}

}
