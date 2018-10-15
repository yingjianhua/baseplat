package com.irille.core.repository.orm.columns;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.ColumnTypes;
import com.irille.core.repository.orm.IColumnField;

import irille.pub.tb.EnumLine;
import irille.pub.tb.IEnumOpt;

public class OptColumn extends Column {

	public IEnumOpt opt;

	public OptColumn(IColumnField field, IEnumOpt opt, String showName, String columnName, String fieldName, ColumnTypes type, boolean unique, boolean primary, boolean autoIncrement, boolean nullable,
			Object defaultValue, String columnDefinition, int length, int precision, int scale, String comment) {
		super(field, showName, columnName, fieldName, ColumnTypes.OPTLINE, unique, primary, autoIncrement, nullable, defaultValue, columnDefinition, length, precision, scale, comment);
		this.opt = opt;
	}
	public IEnumOpt opt() {
		return opt;
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
	    		.append(TAB).append("public ").append(opt.getClass().getSimpleName()).append(" ").append(gtterMethod()).append("() {").append(LN)
	    		.append(TAB+TAB).append("return (").append(opt.getClass().getSimpleName()).append(")(").append(opt.getClass().getSimpleName()).append(".").append(opt.name()).append(".getLine().get(").append(fieldName).append("));").append(LN)
	    		.append(TAB).append("}").append(LN)
	    		.append(TAB).append("public void ").append(stterMethod()).append("(").append(opt.getClass().getSimpleName()).append(" ").append(fieldName()).append(") {").append(LN)
	    		.append(TAB+TAB).append("this.").append(fieldName).append(" = ").append(fieldName()).append(".getLine().getKey();").append(LN)
	    		.append(TAB).append("}").append(LN)
	    		.toString();
		return super.getGetterSetterComment()+gtterStterComment;
	}
	protected String getCommentSql() {
		String comment = this.comment;
		for(EnumLine line:opt.getLine().getLines()) {
			comment+=" "+line.getKey()+":"+line.getName();
		}
		return comment;
	}
	
}
