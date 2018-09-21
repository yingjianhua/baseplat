package com.irille.core.repository.orm;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

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
	OPTLINE(String.class, "TINYINT"),
	BLOB(String.class, "TEXT"),
	CLOB(String.class, "TEXT"),
	CHAR(String.class, "CHAR"),
	STROPT(String.class, "CHAR"),
	JSONOBJECT(JSONObject.class, "JSON"),
	JSONARRAY(JSONArray.class, "JSON"),
	;
	Class<?> javaClass;
	
	String sqlType;
	static final String TAB = "\t";
	static final String LN = "\r\n";
	MessageFormat getterSetterTemplate = new MessageFormat(TAB+"public {0} {1}() '{'"+LN+TAB+TAB+"return {2};"+LN+TAB+"'}'"+LN+TAB+"public void {3}({0} {2}) '{'"+LN+TAB+TAB+"this.{2} = {2};"+LN+TAB+"'}'"+LN);
	MessageFormat fieldCommentTemplate = new MessageFormat(TAB+"private {0} {1}; // {2} {3}({4})<{5}>"+LN);//TAB+"private String _rem;	// 备注  STR(200)<null>";
	MessageFormat initTemplate = new MessageFormat(TAB+TAB+"{0} = {1}; // {2} {3}({4})"+LN) ;
	
	ColumnTypes(Class<?> javaClass, String sqlType) {
		this.javaClass = javaClass;
		this.sqlType = sqlType;
	}
	public String getFieldComment(Column column) {
		return fieldCommentTemplate.format(new String[] {this.javaClass.getSimpleName(), column.fieldName(), column.showName(), this.name(), column.getLength()+"", column.isNullable()?"null":""});
	}
	public String getGetterSetterComment(Column column) {
		return getterSetterTemplate.format(new String[] {this.javaClass.getSimpleName(), column.getterMethod(), column.fieldName(), column.setterMethod()});
	}
	public String getInitComment(Column column) {
		if(column.isPrimary()&&column.isAutoIncrement())
			return "";
		String defaultValue;
		if (column.defaultValue() == null)
			defaultValue =  "null";
		else if (column.defaultValue() instanceof BigDecimal)
			defaultValue = "new BigDecimal("+column.defaultValue().toString()+")";
		else
			defaultValue = column.defaultValue().toString();
		return initTemplate.format(new String[] {column.fieldName(), defaultValue, column.showName(), this.name(), column.getLength()+"",});
	}
}