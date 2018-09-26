package com.irille.core.repository.orm;
import java.math.BigDecimal;

public enum ColumnTemplate implements IColumnTemplate {

	STR(ColumnFactory.type(ColumnTypes.STR)),
	STR_NULL(STR.nullable(true)),
	STR__10(STR.length(10)),
	STR__10_NULL(STR_NULL.length(10)),
	STR__20(STR.length(20)),
	STR__20_NULL(STR_NULL.length(20)),
	STR__50(STR.length(50)),
	STR__50_NULL(STR_NULL.length(50)),
	STR__100(STR.length(100)),
	STR__100_NULL(STR_NULL.length(100)),
	STR__200(STR.length(200)),
	STR__200_NULL(STR_NULL.length(200)),
	STR__2000_NULL(STR_NULL.length(2000)),
	TEXT(ColumnFactory.type(ColumnTypes.TEXT)),
	TEXT_NULL(TEXT.nullable(true)),
	TEXT__100(TEXT.length(100)),
	TEXT__100_NULL(TEXT_NULL.length(100)),
	TEXT__20000(TEXT.length(20000)),
	TEXT__20000_NULL(TEXT_NULL.length(20000)),
	JSON(ColumnFactory.type(ColumnTypes.JSONOBJECT)),
	I18N(ColumnFactory.i18n()),
	
	INT__11(ColumnFactory.type(ColumnTypes.INT).length(11)),
	INT__11_NULL(ColumnFactory.type(ColumnTypes.INT).length(11).nullable(true)),
	INT__11_ZERO(ColumnFactory.type(ColumnTypes.INT).length(11).defaultValue(0)),
	DATA(ColumnFactory.type(ColumnTypes.DATE).nullable(true)),
	TIME(ColumnFactory.type(ColumnTypes.TIME)),
	SHORT(ColumnFactory.type(ColumnTypes.SHORT).length(6)),
	PKEY(INT__11.primary(true).autoIncrement(true)),
	OPTION(ColumnFactory.type(ColumnTypes.OPTLINE).length(4)),
	BOOLEAN(ColumnFactory.type(ColumnTypes.BOOLEAN)),
	AMT(ColumnFactory.type(ColumnTypes.DEC).precision(10).scale(2).defaultValue(BigDecimal.ONE).showName("金额")),
	EMAIL(ColumnTemplate.STR__50_NULL.nullable(true).showName("邮件")),
	QQ(ColumnTemplate.STR__50_NULL.showName("QQ")),
	MOBILE(ColumnTemplate.STR__50_NULL.showName("联系方式")),
	IMG(ColumnTemplate.STR__200_NULL.showName("图片")),
	IMG_2000(ColumnTemplate.STR__2000_NULL.showName("图片")),
	DISCOUNT(ColumnFactory.type(ColumnTypes.DEC).precision(10).scale(2).defaultValue(BigDecimal.ZERO).showName("折扣")),
	BOOLEAN_TRUE(ColumnTemplate.BOOLEAN.defaultValue("1")),
	BOOLEAN_FALSE(ColumnTemplate.BOOLEAN.defaultValue("0")),
		;
	ColumnBuilder builder;
	
	ColumnTemplate(ColumnBuilder columnBuilder) {
		this.builder = columnBuilder;
	}

	public ColumnBuilder builder() {
		return builder;
	}
	
}
