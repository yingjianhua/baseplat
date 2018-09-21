package com.irille.core.repository.orm;

public interface IColumnTemplate {
	
	public ColumnBuilder builder();
	

	default ColumnBuilder copy() {
		return ColumnBuilder.fromBuilder(this.builder());
	}
	default ColumnBuilder showName(String showName) {
		return ColumnBuilder.fromBuilder(this.builder()).showName(showName);
	}
	default ColumnBuilder columnName(String columnName) {
		return ColumnBuilder.fromBuilder(this.builder()).columnName(columnName);
	}
	default ColumnBuilder unique(boolean unique) {
		return ColumnBuilder.fromBuilder(this.builder()).unique(unique);
	}
	default ColumnBuilder primary(boolean primary) {
		return ColumnBuilder.fromBuilder(this.builder()).primary(primary);
	}
	default ColumnBuilder autoIncrement(boolean autoIncrement) {
		return ColumnBuilder.fromBuilder(this.builder()).autoIncrement(autoIncrement);
	}
	default ColumnBuilder nullable(boolean nullable) {
		return ColumnBuilder.fromBuilder(this.builder()).nullable(nullable);
	}
	default ColumnBuilder columnDefinition(String columnDefinition) {
		return ColumnBuilder.fromBuilder(this.builder()).columnDefinition(columnDefinition);
	}
	default ColumnBuilder length(int length) {
		return ColumnBuilder.fromBuilder(this.builder()).length(length);
	}
	default ColumnBuilder precision(int precision) {
		return ColumnBuilder.fromBuilder(this.builder()).precision(precision);
	}
	default ColumnBuilder scale(int scale) {
		return ColumnBuilder.fromBuilder(this.builder()).scale(scale);
	}
	default ColumnBuilder comment(String comment) {
		return ColumnBuilder.fromBuilder(this.builder()).comment(comment);
	}
}
