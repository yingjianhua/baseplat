package com.irille.core.repository.orm;

public enum ColumnTemplate implements IColumnTemplate {

	STR(ColumnFactory.type(ColumnTypes.STR)),
	STR__100(STR.length(100)),
	STR__100_NULL(STR.length(100).nullable(true)),
	STR__200(STR.length(200)),
	EMAIL(STR__100_NULL.showName("邮箱地址")),
	
	JSON(ColumnFactory.type(ColumnTypes.JSONOBJECT)),
	I18N(JSON.copy()),
	
	INT__11(ColumnFactory.type(ColumnTypes.INT)),
	PKEY(INT__11.primary(true).autoIncrement(true)),
	
	OPTION(ColumnFactory.type(ColumnTypes.OPTLINE)),
	;
	ColumnBuilder builder;
	
	ColumnTemplate(ColumnBuilder columnBuilder) {
		this.builder = columnBuilder;
	}

	public ColumnBuilder builder() {
		return builder;
	}
	
}
