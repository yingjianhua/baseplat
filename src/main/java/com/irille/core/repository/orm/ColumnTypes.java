package com.irille.core.repository.orm;

import java.math.BigDecimal;
import java.util.Date;

public enum ColumnTypes {
	BYTE(Byte.class, "TINYINT"),
	BOOLEAN(Boolean.class, "TINYINT"),
	SHORT(Short.class, "SMALLINT"),
	INT(Integer.class, "INT"),
	LONG(Long.class, "BIGINT"),
	DEC(BigDecimal.class, "DECIMAL"),
	STR(String.class, "VARCHAR"),
	DATE(Date.class, "DATE"),
	TIME(Date.class, "DATETIME"),
	DOUBLE(Double.class, "DOUBLE"),
	STRINGTEXT(String.class, "MEDIUMTEXT"),
	TEXT(String.class, "TEXT"),
	OPTLINE(Byte.class, "TINYINT"),
	BLOB(String.class, "TEXT"),
	CLOB(String.class, "TEXT"),
	CHAR(String.class, "CHAR"),
	STROPT(String.class, "CHAR"),
	JSONOBJECT(String.class, "JSON"),
	JSONARRAY(String.class, "JSON"),
	;
	Class<?> javaClass;
	private String sqlType;
	static final String TAB = "\t";
	static final String LN = "\r\n";
	private String getterSetterTemplate = TAB+"public {0} {1}() '{'"+LN+TAB+TAB+"return {2};"+LN+TAB+"'}'"+LN+TAB+"public void {3}({0} {2}) '{'"+LN+TAB+TAB+"this.{2} = {2};"+LN+TAB+"'}'"+LN;
	private String fieldCommentTemplate = TAB+"private {0} {1}; // {2}{3} {4}({5}){6}"+LN+"{7}";//TAB+"private String _rem;	// 备注  STR(200)<null>";
	private String initTemplate = TAB+TAB+"{0} = {1}; // {2}{3} {4}({5})"+LN;

	ColumnTypes(Class<?> javaClass, String sqlType) {
		this.javaClass = javaClass;
		this.sqlType = sqlType;
	}
	public String getGetterSetterTemplate() {
		return getterSetterTemplate;
	}
	public String getFieldCommentTemplate() {
		return fieldCommentTemplate;
	}
	public String getInitTemplate() {
		return initTemplate;
	}
	public String sqlType() {
		return sqlType;
	}
	public Class<?> javaClass() {
		return javaClass;
	}

}