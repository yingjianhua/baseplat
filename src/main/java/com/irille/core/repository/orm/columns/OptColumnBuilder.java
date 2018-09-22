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
    	if(this.columnName()==null)
    		this.columnName(field.name().toLowerCase());
    	String fieldName = Str.tranLineUpperToField(field.name().indexOf("__")<0?field.name():field.name().substring(0, field.name().indexOf("__")));
    	if(length()==0)
    		this.length(4);
    	if(showName()==null)
    		this.showName(fieldName);
    	if(comment()==null)
    		comment(showName());
    	if(defaultValue() != null)
    		assertTrue(opt.getClass()==defaultValue().getClass());
    	if(nullable()==false&&defaultValue()==null)
    		defaultValue(opt);
    	return new OptColumn(opt, showName(), columnName(), fieldName, type(), unique(), primary(), autoIncrement(), nullable(), defaultValue(), columnDefinition(), length(), precision(), scale(), comment()); 
    }
    
    public OptColumnBuilder opt(IEnumOpt opt) {
    	this.opt = opt;
    	return this;
    }
    
    public IEnumOpt opt() {
    	return opt;
    }
    
}
