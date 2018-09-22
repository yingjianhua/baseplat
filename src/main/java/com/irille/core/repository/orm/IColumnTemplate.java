package com.irille.core.repository.orm;

public interface IColumnTemplate {
	
	public ColumnBuilder builder();
	

	default ColumnBuilder copy() {
		return ColumnFactory.fromBuilder(this.builder());
	}
	default ColumnBuilder showName(String showName) {
		return ColumnFactory.fromBuilder(this.builder()).showName(showName);
	}
	default ColumnBuilder columnName(String columnName) {
		return ColumnFactory.fromBuilder(this.builder()).columnName(columnName);
	}
	default ColumnBuilder unique(boolean unique) {
		return ColumnFactory.fromBuilder(this.builder()).unique(unique);
	}
	default ColumnBuilder primary(boolean primary) {
		return ColumnFactory.fromBuilder(this.builder()).primary(primary);
	}
	default ColumnBuilder autoIncrement(boolean autoIncrement) {
		return ColumnFactory.fromBuilder(this.builder()).autoIncrement(autoIncrement);
	}
	default ColumnBuilder nullable(boolean nullable) {
		return ColumnFactory.fromBuilder(this.builder()).nullable(nullable);
	}
	default ColumnBuilder defaultValue(Object defaultValue) {
		return ColumnFactory.fromBuilder(this.builder()).defaultValue(defaultValue);
	}
	default ColumnBuilder columnDefinition(String columnDefinition) {
		return ColumnFactory.fromBuilder(this.builder()).columnDefinition(columnDefinition);
	}
	default ColumnBuilder length(int length) {
		return ColumnFactory.fromBuilder(this.builder()).length(length);
	}
	default ColumnBuilder precision(int precision) {
		return ColumnFactory.fromBuilder(this.builder()).precision(precision);
	}
	default ColumnBuilder scale(int scale) {
		return ColumnFactory.fromBuilder(this.builder()).scale(scale);
	}
	default ColumnBuilder comment(String comment) {
		return ColumnFactory.fromBuilder(this.builder()).comment(comment);
	}
}
