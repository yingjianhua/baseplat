package com.irille.core.repository.orm;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.MessageFormat;

import com.irille.core.repository.orm.columns.OneToManyColumn;
import com.irille.core.repository.orm.columns.OptColumn;

import irille.pub.tb.EnumLine;
import irille.pub.tb.IEnumOpt;

public abstract class Column {

	protected String showName;
	
	protected String columnName;
	
	protected String fieldName;
	
	protected ColumnTypes type;
	
	protected boolean unique;
    
	protected boolean primary;
    
	protected boolean autoIncrement;

	protected boolean nullable;
    
	protected Object defaultValue;

	protected String columnDefinition;

	protected Table<?> table;

	protected int length;

	protected int precision;

	protected int scale;
    
	protected String comment;
	
	private Method getterMethod;
	
	private Method setterMethod;
    
    public Column(String showName, String columnName, String fieldName, ColumnTypes type, boolean unique, boolean primary, boolean autoIncrement, boolean nullable, Object defaultValue, String columnDefinition, int length,
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
		this.defaultValue = defaultValue;
		this.columnDefinition = columnDefinition;
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
    public Method getterMethod() {
    	if(getterMethod == null)
			try {
				getterMethod = table.entity().getMethod(getterMethodString());
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
    	return getterMethod;
    }
    public Method setterMethod() {
    	if(setterMethod == null)
			try {
				setterMethod = table.entity().getMethod(setterMethodString(), this.type().javaClass);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
    	return setterMethod;
    }
    public String getterMethodString() {
    	return "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
    }
    public String setterMethodString() {
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
	protected String getCommentSql() {
		return comment;
	}
	
	protected static final String TAB = "\t";
	protected static final String LN = "\r\n";
	
	public String getFieldComment() {
		
		String specialColumn = "";
		
		String isNull = "";
		if(isNullable())
			isNull = "<null>";
		
		String remark = "";
		
		if(this instanceof OptColumn) {
			OptColumn optColumn = (OptColumn)this;
			specialColumn = "<"+optColumn.opt().getClass().getSimpleName()+">";
			
			remark = "";
			for(EnumLine line:optColumn.opt.getLine().getLines()) {
				remark+=TAB+"// " + line.getVarName() + ":" + line.getKey()+","+line.getName()+LN;
			}
		} else if(this instanceof OneToManyColumn) {
			OneToManyColumn oneToManyColumn = (OneToManyColumn)this;
			specialColumn = "<表主键:"+oneToManyColumn.targetEntity().getSimpleName()+">";
		}
		return new MessageFormat(type.getFieldCommentTemplate()).format(new String[] {type.javaClass.getSimpleName(), fieldName(), showName(), specialColumn, type.sqlType(), getLength()+"", isNull, remark});
	}
	public String getGetterSetterComment() {
		return new MessageFormat(type.getGetterSetterTemplate()).format(new String[] {type.javaClass.getSimpleName(), getterMethodString(), fieldName(), setterMethodString()});
	}
	public String getInitComment() {
		if(isPrimary()&&isAutoIncrement())
			return "";
		String defaultValue;
		if (defaultValue() == null)
			defaultValue =  "null";
		else if (this.defaultValue == "")
			defaultValue = "\"\"";
		else if (defaultValue() instanceof BigDecimal)
			defaultValue = "new BigDecimal("+defaultValue().toString()+")";
		else if(defaultValue() instanceof IEnumOpt) {
			IEnumOpt o = (IEnumOpt)defaultValue();
			defaultValue = o.getClass().getSimpleName()+"."+o.name()+".getLine().getKey()";
		} else
			defaultValue = defaultValue().toString();
		
		String specialColumn = "";
		if(this instanceof OptColumn)
			specialColumn = "<"+((OptColumn)this).opt().getClass().getSimpleName()+">";
		return new MessageFormat(type.getInitTemplate()).format(new String[] {fieldName(), defaultValue, showName(), specialColumn, type.sqlType(), getLength()+"",});
	}
	public String createSql() {
		if(columnDefinition!=null && !"".equals(columnDefinition))
			return columnDefinition;
		StringBuilder s = new StringBuilder();
		s.append(columnName).append(" ").append(type.sqlType());
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
		if(defaultValue!=null) {
			s.append(" default '");
			if(defaultValue instanceof IEnumOpt) {
				IEnumOpt o = (IEnumOpt)defaultValue;
				s.append(o.getLine().getKey());
			} else {
				s.append(defaultValue.toString());
			}
			s.append("'");
		}
		if(getCommentSql()!=null&&!"".equals(getCommentSql()))
			s.append(" comment '").append(getCommentSql()).append("'");
    	return s.toString();
    }
	
}
