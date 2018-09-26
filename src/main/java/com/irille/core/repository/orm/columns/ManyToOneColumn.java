package com.irille.core.repository.orm.columns;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.ColumnTypes;
import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.orm.IColumnField;
import com.irille.core.repository.orm.Table;

import irille.pub.ClassTools;

public class ManyToOneColumn<T extends Entity> extends Column {

	private Class<T> targetEntity;

	public ManyToOneColumn(IColumnField field, Class<T> targetEntity, String showName, String columnName, String fieldName, ColumnTypes type, boolean unique, boolean primary, boolean autoIncrement, boolean nullable,
			Object defaultValue, String columnDefinition, int length, int precision, int scale, String comment) {
		super(field, showName, columnName, fieldName, type, unique, primary, autoIncrement, nullable, defaultValue, columnDefinition, length, precision, scale, comment);
		this.targetEntity = targetEntity;
	}

	public String gtterMethod() {
    	return "gt"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
    }
    public String stterMethod() {
    	return "st"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
    }
    
	public String getGetterSetterComment() {
		String gtterStterComment = 
	    		new StringBuilder()
	    		.append(TAB).append("public ").append(targetEntity.getSimpleName()).append(" ").append(gtterMethod()).append("() {").append(LN)
	    		.append(TAB+TAB).append("return SELECT(").append(targetEntity.getSimpleName()).append(".class, ").append(getterMethodString()).append("());").append(LN)
	    		.append(TAB).append("}").append(LN)
	    		.append(TAB).append("public void ").append(stterMethod()).append("(").append(targetEntity.getSimpleName()).append(" ").append(fieldName()).append(") {").append(LN)
	    		.append(TAB+TAB).append("this.").append(fieldName).append(" = ").append(fieldName()).append(".").append(((Table<?>)ClassTools.getStaticProerty(targetEntity, "table")).primaryKey().getterMethodString()).append("();").append(LN)
	    		.append(TAB).append("}").append(LN)
	    		.toString();
		return super.getGetterSetterComment()+gtterStterComment;
	}
	protected String getCommentSql() {
		String comment = this.getComment();
		Table<?> table = (Table<?>)ClassTools.getStaticProerty(targetEntity, "table");
		comment += " 外键关联:"+table.name()+"."+table.primaryKey().columnName();
		return comment;
	}

	public Class<T> targetEntity() {
		return targetEntity;
	}
	
}
