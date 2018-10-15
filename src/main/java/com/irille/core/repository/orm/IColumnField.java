package com.irille.core.repository.orm;

import java.io.Serializable;

import com.irille.core.repository.query.Predicate;

public interface IColumnField {

	public Column column();
	
	public String name();
	
	default Predicate as(String alias) {
		return new Predicate(column()).alias(alias);
	}
	default Predicate eq(Serializable param) {
		return new Predicate(column()).eq(param);
	}
	default Predicate gt(Serializable param) {
		return new Predicate(column()).gt(param);
	}
	default Predicate lt(Serializable param) {
		return new Predicate(column()).lt(param);
	}
	default Predicate between(Serializable param, Serializable param2) {
		return new Predicate(column()).between(param, param2);
	}
	default Predicate like(Serializable param) {
		return new Predicate(column()).like(param);
	}
	default String columnName() {
    	return this.column().columnName();
    }
	default String columnFullName() {
    	return this.column().columnFullName();
    }
	default String columnNameWithAlias() {
    	return this.column().columnNameWithAlias();
    }
	default String fieldName() {
		return this.column().fieldName();
	}
}
