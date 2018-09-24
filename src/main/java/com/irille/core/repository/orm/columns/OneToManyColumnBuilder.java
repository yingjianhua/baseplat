package com.irille.core.repository.orm.columns;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.ColumnBuilder;
import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.orm.IColumnField;
import com.irille.core.repository.orm.Table;

import irille.pub.ClassTools;
import irille.pub.Str;

public class OneToManyColumnBuilder<T extends Entity> extends ColumnBuilder {
    
    Class<T> targetEntity;

    public OneToManyColumnBuilder(Class<T> targetEntity) {
    	super();
    	Table<?> table = (Table<?>)ClassTools.getStaticProerty(targetEntity, "table");
    	Column outKey = table.primaryKey();
    	targetEntity(targetEntity);
    	type(outKey.type());
		length(outKey.getLength());
		precision(outKey.getPrecision());
		scale(outKey.getScale());
    }
    
    public Column create(IColumnField field) {
    	String columnName = this.columnName()==null?field.name().toLowerCase():this.columnName();
    	String fieldName = Str.tranLineUpperToField(field.name().indexOf("__")<0?field.name():field.name().substring(0, field.name().indexOf("__")));
    	String showName = this.showName()==null?fieldName:this.showName();
    	String comment = this.comment()==null?showName:this.comment();
    	return new OneToManyColumn(targetEntity, showName, columnName, fieldName, type(), unique(), primary(), autoIncrement(), nullable(), defaultValue(), columnDefinition(), length(), precision(), scale(), comment); 
    }
    
    public void targetEntity(Class<T> targetEntity) {
    	this.targetEntity = targetEntity;
    }
    public Class<T> targetEntity() {
    	return targetEntity;
    }
    
}
