package com.irille.core.repository.orm;

public class Column {

	String showName;
	
	String columnName;
	
	String fieldName;
	
	ColumnTypes type;
	
    boolean unique;
    
    boolean primary;
    
    boolean autoIncrement;

    boolean nullable;
    
    Object defaultValue;//TODO

    String columnDefinition;

    Table<?> table;

    int length;

    int precision;

    int scale;
    
    String comment;
    
    public Column(String showName, String columnName, String fieldName, ColumnTypes type, boolean unique, boolean primary, boolean autoIncrement, boolean nullable, String columnDefinition, Table<?> table, int length,
			int precision, int scale, String comment) {
		super();
		this.showName = showName;
		this.columnName = columnName;
		this.fieldName = fieldName;
		this.type = type;
		this.unique = unique;
		this.primary = primary;
		this.autoIncrement = autoIncrement;
		this.nullable = nullable;
		this.columnDefinition = columnDefinition;
		this.table = table;
		this.length = length;
		this.precision = precision;
		this.scale = scale;
		this.comment = comment;
	}
	public String showName() {
		return showName;
	}
    public String columnName() {
    	return columnName;
    }
    public String fieldName() {
    	return fieldName;
    }
    public String getterMethod() {
    	return "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
    }
    public String setterMethod() {
    	return "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
    }
    public ColumnTypes type() {
    	return type;
    }
	public boolean isUnique() {
		return unique;
	}
	public boolean isPrimary() {
		return primary;
	}
	public boolean isAutoIncrement() {
		return autoIncrement;
	}
	public boolean isNullable() {
		return nullable;
	}
	public String getColumnDefinition() {
		return columnDefinition;
	}
	public Object defaultValue() {
		return defaultValue;
	}
	public Table<?> getTable() {
		return table;
	}
	public int getLength() {
		return length;
	}
	public int getPrecision() {
		return precision;
	}
	public int getScale() {
		return scale;
	}
	public String getComment() {
		return comment;
	}
	public String createSql() {
		if(columnDefinition!=null && !"".equals(columnDefinition))
			return columnDefinition;
		StringBuilder s = new StringBuilder();
		s.append(columnName).append(" ").append(type.sqlType);
		if(precision>0) {
			s.append("(").append(precision);
			if(scale>0)
				s.append(",").append(scale);
			s.append(")");
		} else if(length>0){
			s.append("(").append(length).append(")");
		}
		if(!nullable)
			s.append(" NOT");
		s.append(" NULL");
		if(isAutoIncrement())
			s.append(" auto_increment");
		if(comment!=null&&!"".equals(comment))
			s.append(" comment '").append(comment).append("'");
    	return s.toString();
    }
	
}
