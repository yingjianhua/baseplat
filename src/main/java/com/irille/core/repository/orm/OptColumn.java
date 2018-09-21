package com.irille.core.repository.orm;

import irille.pub.tb.IEnumOpt;

public class OptColumn extends Column {

	public IEnumOpt opt;

	public OptColumn(IEnumOpt opt, String showName, String columnName, String fieldName, ColumnTypes type, boolean unique, boolean primary, boolean autoIncrement, boolean nullable,
			String columnDefinition, Table<?> table, int length, int precision, int scale, String comment) {
		super(showName, columnName, fieldName, ColumnTypes.OPTLINE, unique, primary, autoIncrement, nullable, columnDefinition, table, length, precision, scale, comment);
		this.opt = opt;
	}

}
