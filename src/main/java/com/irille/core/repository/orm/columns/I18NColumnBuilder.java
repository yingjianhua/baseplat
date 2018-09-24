package com.irille.core.repository.orm.columns;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.ColumnBuilder;
import com.irille.core.repository.orm.IColumnField;

import irille.pub.Str;

public class I18NColumnBuilder extends ColumnBuilder {

	public I18NColumnBuilder() {
	}
    
    public Column create(IColumnField field) {
    	String columnName = this.columnName()==null?field.name().toLowerCase():this.columnName();
    	String fieldName = Str.tranLineUpperToField(field.name().indexOf("__")<0?field.name():field.name().substring(0, field.name().indexOf("__")));
    	String showName = this.showName()==null?fieldName:this.showName();
    	String comment = this.comment()==null?showName:this.comment();
    	return new I18NColumn(field, showName, columnName, fieldName, type(), unique(), primary(), autoIncrement(), nullable(), defaultValue(), columnDefinition(), length(), precision(), scale(), comment); 
    }
    
}
