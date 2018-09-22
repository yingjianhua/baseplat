package com.irille.core.repository.orm.columns;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.ColumnTypes;
import com.irille.core.repository.orm.Table;

public class I18NColumn extends Column{

	public I18NColumn(String showName, String columnName, String fieldName, ColumnTypes type, boolean unique, boolean primary, boolean autoIncrement, boolean nullable, Object defaultValue,
			String columnDefinition, int length, int precision, int scale, String comment) {
		super(showName, columnName, fieldName, ColumnTypes.JSONOBJECT, unique, primary, autoIncrement, nullable, defaultValue, columnDefinition, length, precision, scale, comment);
	}
}
