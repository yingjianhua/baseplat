package com.irille.core.repository.orm;

public interface IColumnField {

	public Column column();
	
	public String name();
	
//	default Column belong(IColumnField field) {
//		if(this.column().columnName==null)
//			this.column().columnName=field.name().toLowerCase();
//		return this.column();
//	}
	
//	default ColumnBuilder columnName(String columnName) {
//		return ColumnBuilder.fromColumn(this.column()).columnName(columnName);
//	}
//	default ColumnBuilder unique(boolean unique) {
//		return ColumnBuilder.fromColumn(this.column()).unique(unique);
//	}
//	default ColumnBuilder primary(boolean primary) {
//		return ColumnBuilder.fromColumn(this.column()).primary(primary);
//	}
//	default ColumnBuilder autoIncrement(boolean autoIncrement) {
//		return ColumnBuilder.fromColumn(this.column()).autoIncrement(autoIncrement);
//	}
//	default ColumnBuilder nullable(boolean nullable) {
//		return ColumnBuilder.fromColumn(this.column()).nullable(nullable);
//	}
//	default ColumnBuilder columnDefinition(String columnDefinition) {
//		return ColumnBuilder.fromColumn(this.column()).columnDefinition(columnDefinition);
//	}
//	default ColumnBuilder length(int length) {
//		return ColumnBuilder.fromColumn(this.column()).length(length);
//	}
//	default ColumnBuilder precision(int precision) {
//		return ColumnBuilder.fromColumn(this.column()).precision(precision);
//	}
//	default ColumnBuilder scale(int scale) {
//		return ColumnBuilder.fromColumn(this.column()).scale(scale);
//	}
//	default ColumnBuilder comment(String comment) {
//		return ColumnBuilder.fromColumn(this.column()).comment(comment);
//	}
	
}
