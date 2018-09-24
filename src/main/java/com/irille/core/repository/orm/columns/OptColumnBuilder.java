package com.irille.core.repository.orm.columns;

import static org.junit.Assert.assertTrue;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.ColumnBuilder;
import com.irille.core.repository.orm.IColumnField;

import irille.pub.Str;
import irille.pub.tb.IEnumOpt;

public class OptColumnBuilder extends ColumnBuilder {
	
	IEnumOpt opt;
	
    public OptColumnBuilder(IEnumOpt opt) {
    	this.opt(opt);
    }

    public Column create(IColumnField field) {
    	String columnName = this.columnName()==null?field.name().toLowerCase():this.columnName();
    	String fieldName = Str.tranLineUpperToField(field.name().indexOf("__")<0?field.name():field.name().substring(0, field.name().indexOf("__")));
    	String showName = this.showName()==null?fieldName:this.showName();
    	String comment = this.comment()==null?showName:this.comment();
    	int length = this.length()==0?4:this.length();
    	if(defaultValue() != null)
    		assertTrue(opt.getClass()==defaultValue().getClass());
    	Object defaultValue = (nullable()==false&&defaultValue()==null)?opt:this.defaultValue();
    	return new OptColumn(opt, showName, columnName, fieldName, type(), unique(), primary(), autoIncrement(), nullable(), defaultValue, columnDefinition(), length, precision(), scale(), comment); 
    }
    
    public OptColumnBuilder opt(IEnumOpt opt) {
    	this.opt = opt;
    	return this;
    }
    
    public IEnumOpt opt() {
    	return opt;
    }
    
}
