package com.irille.core.repository.orm.columns;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.ColumnBuilder;
import com.irille.core.repository.orm.ColumnTypes;
import com.irille.core.repository.orm.IColumnField;

import irille.pub.Str;

public class I18NColumnBuilder extends ColumnBuilder {

	public I18NColumnBuilder() {
	}
    
    public Column create(IColumnField field) {
    	if(this.columnName()==null)
    		this.columnName(field.name().toLowerCase());
    	String fieldName = Str.tranLineUpperToField(field.name().indexOf("__")<0?field.name():field.name().substring(0, field.name().indexOf("__")));
    	if(showName()==null)
    		this.showName(fieldName);
    	if(comment()==null)
    		comment(showName());
    	return new I18NColumn(showName(), columnName(), fieldName, type(), unique(), primary(), autoIncrement(), nullable(), defaultValue(), columnDefinition(), length(), precision(), scale(), comment()); 
    }
    
}
