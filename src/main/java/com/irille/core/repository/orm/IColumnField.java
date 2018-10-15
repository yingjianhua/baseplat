package com.irille.core.repository.orm;

import java.io.Serializable;

import com.irille.core.repository.query.IPredicate;
import com.irille.core.repository.query.IPredicateBuilder;
import com.irille.core.repository.query.Predicate;

public interface IColumnField extends IPredicateBuilder, IPredicate {

	public Column column();
	
	public String name();
	
	default String fieldName() {
		return this.column().fieldName();
	}
	@Override
	default Predicate as(String alias) {
		return new Predicate(column()).alias(alias);
	}
	@Override
	default Predicate eq(Serializable param) {
		return new Predicate(column()).eq(param);
	}
	@Override
	default Predicate ne(Serializable param) {
		return new Predicate(column()).ne(param);
	}
	@Override
	default Predicate gt(Serializable param) {
		return new Predicate(column()).gt(param);
	}
	@Override
	default Predicate ge(Serializable param) {
		return new Predicate(column()).ge(param);
	}
	@Override
	default Predicate lt(Serializable param) {
		return new Predicate(column()).lt(param);
	}
	@Override
	default Predicate le(Serializable param) {
		return new Predicate(column()).le(param);
	}
	@Override
	default Predicate isNull() {
		return new Predicate(column()).isNull();
	}
	@Override
	default Predicate notNull() {
		return new Predicate(column()).notNull();
	}
	@Override
	default Predicate between(Serializable param, Serializable param2) {
		return new Predicate(column()).between(param, param2);
	}
	@Override
	default Predicate like(Serializable param) {
		return new Predicate(column()).like(param);
	}
	@Override
	default Predicate desc() {
		return new Predicate(column()).desc();
	}
	@Override
	default Predicate asc() {
		return new Predicate(column()).asc();
	}
	@Override
	default Predicate params(Serializable... param) {
		return new Predicate(column()).params(param);
	}
	@Override
	default Predicate value(Serializable value) {
		return new Predicate(column()).value(value);
	}

	@Override
	default String columnName() {
    	return this.column().columnName();
    }
	@Override
	default String columnFullName() {
    	return this.column().columnFullName();
    }
	@Override
	default String columnNameWithAlias() {
    	return this.column().columnFullName()+" as "+this.column().fieldName();
    }
	@Override
	default String alias() {
		return this.column().fieldName();
	}

	@Override
	default String getConditions() {
		return "";
	}
	@Override
	default Serializable[] getParams() {
		return new Serializable[] {};
	}
	
}
