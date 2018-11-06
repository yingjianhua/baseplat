package com.irille.core.repository.orm;
import java.math.BigDecimal;

import irille.pub.tb.EnumLine;
import irille.pub.tb.IEnumOpt;

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
	STR__500(STR.length(500)),
	STR__500_NULL(STR_NULL.length(500)),
	STR__1000(STR.length(1000)),
	STR__1000_NULL(STR_NULL.length(1000)),
	STR__2000(STR.length(2000)),
	STR__2000_NULL(STR_NULL.length(2000)),
	TEXT(ColumnFactory.type(ColumnTypes.TEXT)),
	TEXT_NULL(TEXT.nullable(true)),
	TEXT__100(TEXT.length(100)),
	TEXT__100_NULL(TEXT_NULL.length(100)),
	TEXT__20000(TEXT.length(20000)),
	TEXT__20000_NULL(TEXT_NULL.length(20000)),
	JSON(ColumnFactory.type(ColumnTypes.JSONOBJECT)),
	I18N(ColumnFactory.i18n()),
	EMAIL(ColumnTemplate.STR__50_NULL.nullable(true).showName("邮件")),
	QQ(ColumnTemplate.STR__50_NULL.showName("QQ")),
	MOBILE(ColumnTemplate.STR__50_NULL.showName("联系方式")),
	IMG(ColumnTemplate.STR__200_NULL.showName("图片")),
	IMG_2000(ColumnTemplate.STR__2000_NULL.showName("图片")),
	
	INT__11(ColumnFactory.type(ColumnTypes.INT).length(11)),
	INT__11_NULL(ColumnFactory.type(ColumnTypes.INT).length(11).nullable(true)),
	INT__11_ZERO(ColumnFactory.type(ColumnTypes.INT).length(11).defaultValue(0)),
	PKEY(INT__11.primary(true).autoIncrement(true).showName("主键")),
	
	DATA(ColumnFactory.type(ColumnTypes.DATE).nullable(true)),
	TIME(ColumnFactory.type(ColumnTypes.TIME)),
	
	SHORT(ColumnFactory.type(ColumnTypes.SHORT).length(6)),
	SHORT_NULL(ColumnFactory.type(ColumnTypes.SHORT).nullable(true).length(6)),
	ROW_VERSION(SHORT.showName("行版本")),
	
	OPTION(ColumnFactory.type(ColumnTypes.OPTLINE).length(4)),
	SEX(ColumnFactory.opt(OSex.UNKNOW).showName("性别")),
	
	BOOLEAN(ColumnFactory.type(ColumnTypes.BOOLEAN)),
	BOOLEAN_TRUE(ColumnTemplate.BOOLEAN.defaultValue("1")),
	BOOLEAN_FALSE(ColumnTemplate.BOOLEAN.defaultValue("0")),
	
	AMT(ColumnFactory.type(ColumnTypes.DEC).precision(10).scale(2).defaultValue(BigDecimal.ONE).showName("金额")),
	DISCOUNT(ColumnFactory.type(ColumnTypes.DEC).precision(10).scale(2).defaultValue(BigDecimal.ZERO).showName("折扣")),
	
	;
	ColumnBuilder builder;
	
	ColumnTemplate(ColumnBuilder columnBuilder) {
		this.builder = columnBuilder;
	}

	public ColumnBuilder builder() {
		return builder;
	}
	
	public enum OSex implements IEnumOpt {
		UNKNOW(0,"未知"), MALE(1,"男"), FEMALE(2,"女");
		public static String NAME="性别";
		private EnumLine _line;
		private OSex(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}
}
