package com.irille.core.repository.orm;

import com.irille.core.repository.query.Predicate;

public interface IColumnField {

	public Column column();
	
	public String name();
	
	default Predicate as(String alias) {
		return new Predicate(column()).alias(alias);
	}
}
