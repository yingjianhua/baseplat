package com.irille.core.repository.orm;

import lombok.Getter;

@Getter
public abstract class ColumnBuilder {

	String showName;
	
	String columnName;
	
	ColumnTypes type;
	
	Object defaultValue;
	
    boolean unique;
    
    boolean primary;
    
    boolean autoIncrement;

    boolean nullable;

    String columnDefinition;

    Table<?> table;
    
    int length;

    int precision;

    int scale;
    
    String comment;
    
    protected ColumnBuilder() {
    }
    
    public ColumnBuilder(ColumnTypes type) {
    	this.type = type;
    }
    
    public abstract Column create(IColumnField field);
    
    public ColumnBuilder columnName(String columnName) {
    	this.columnName = columnName;
    	return this;
    }
    public String columnName() {
    	return columnName;
    }
    public ColumnBuilder showName(String showName) {
    	this.showName = showName;
    	return this;
    }
    public String showName() {
    	return showName;
    }
    public ColumnBuilder unique(boolean unique) {
    	this.unique = unique;
    	return this;
    }
    public boolean unique() {
    	return unique;
    }
    public ColumnBuilder primary(boolean primary) {
    	this.primary = primary;
    	return this;
    }
    public boolean primary() {
    	return primary;
    }
    public ColumnBuilder autoIncrement(boolean autoIncrement) {
    	this.autoIncrement = autoIncrement;
    	return this;
    }
    public boolean autoIncrement() {
    	return autoIncrement;
    }
    public ColumnBuilder nullable(boolean nullable) {
    	this.nullable = nullable;
    	return this;
    }
    public boolean nullable() {
    	return nullable;
    }
    public ColumnBuilder defaultValue(Object defaultValue) {
    	this.defaultValue = defaultValue;
    	return this;
    }
    public Object defaultValue() {
    	return defaultValue;
    }
    public ColumnBuilder columnDefinition(String columnDefinition) {
    	this.columnDefinition = columnDefinition;
    	return this;
    }
    public String columnDefinition() {
    	return columnDefinition;
    }
    public ColumnBuilder length(int length) {
    	this.length = length;
    	return this;
    }
    public int length() {
    	return length;
    }
    public ColumnBuilder precision(int precision) {
    	this.precision = precision;
    	return this;
    }
    public int precision() {
    	return precision;
    }
    public ColumnBuilder scale(int scale) {
    	this.scale = scale;
    	return this;
    }
    public int scale() {
    	return scale;
    }
    public ColumnBuilder comment(String comment) {
    	this.comment = comment;
    	return this;
    }
    public String comment() {
    	return comment;
    }
    public ColumnBuilder type(ColumnTypes type) {
    	this.type = type;
    	return this;
    }
    public ColumnTypes type() {
    	return type;
    }
    
}
