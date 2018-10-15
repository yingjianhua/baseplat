package com.irille.core.repository.query;

import java.io.Serializable;

public interface IPredicate {

	public String columnName();

	public String columnFullName();

	public String columnNameWithAlias();
	
	public String alias();

	public String getConditions();
	
	public Serializable[] getParams();
}
