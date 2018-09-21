package com.irille.core.repository.orm;

import irille.pub.Str;
import irille.pub.tb.IEnumOpt;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnBuilder {

	String showName;
	
	String columnName;
	
	ColumnTypes type;
	
	IEnumOpt opt;
	
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
    
    public ColumnBuilder(ColumnTypes type) {
    	this.type = type;
    }
    
    public static ColumnBuilder fromBuilder(ColumnBuilder builder) {
    	return new ColumnBuilder(builder.type) {{
    		setShowName(builder.showName);
    		setColumnName(builder.columnName);
    		setUnique(builder.unique);
    		setPrimary(builder.primary);
    		setAutoIncrement(builder.autoIncrement);
    		setNullable(builder.nullable);
    		setUnique(builder.unique);
    		setLength(builder.length);
    		setPrecision(builder.precision);
    		setScale(builder.scale);
    	}};
    }
    
    public Column create(IColumnField field) {
    	if(this.columnName==null)
    		this.columnName=field.name().toLowerCase();
    	String fieldName = Str.tranLineUpperToField(field.name().indexOf("__")<0?field.name():field.name().substring(0, field.name().indexOf("__")));
    	if(showName==null)
    		this.showName=fieldName;
    	if(comment==null)
    		comment = showName;
    	Column column;
    	
    	switch (type) {
		case OPTLINE:
			column = new OptColumn(opt, showName, columnName, fieldName, type, unique, primary, autoIncrement, nullable, columnDefinition, table, length, precision, scale, comment);
			break;
		default:
			column = new Column(showName, columnName, fieldName, type, unique, primary, autoIncrement, nullable, columnDefinition, table, length, precision, scale, comment);
			break;
		}
    	return column; 
    }
    
    public ColumnBuilder columnName(String columnName) {
    	this.columnName = columnName;
    	return this;
    }
    public ColumnBuilder showName(String showName) {
    	this.showName = showName;
    	return this;
    }
    public ColumnBuilder unique(boolean unique) {
    	this.unique = unique;
    	return this;
    }
    public ColumnBuilder primary(boolean primary) {
    	this.primary = primary;
    	return this;
    }
    public ColumnBuilder autoIncrement(boolean autoIncrement) {
    	this.autoIncrement = autoIncrement;
    	return this;
    }
    public ColumnBuilder nullable(boolean nullable) {
    	this.nullable = nullable;
    	return this;
    }
    public ColumnBuilder columnDefinition(String columnDefinition) {
    	this.columnDefinition = columnDefinition;
    	return this;
    }
    public ColumnBuilder length(int length) {
    	this.length = length;
    	return this;
    }
    public ColumnBuilder precision(int precision) {
    	this.precision = precision;
    	return this;
    }
    public ColumnBuilder scale(int scale) {
    	this.scale = scale;
    	return this;
    }
    public ColumnBuilder comment(String comment) {
    	this.comment = comment;
    	return this;
    }
    
}
