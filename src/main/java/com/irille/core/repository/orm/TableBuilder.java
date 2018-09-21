package com.irille.core.repository.orm;


import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import irille.pub.Str;

public class TableBuilder<T extends Entity> {
	
	private String name;
	private Class<T> entity;
	private Index[] indexes = new Index[] {};
	private Column[] columns = new Column[] {};
	private String comment;
	
	public TableBuilder(Class<T> entityClass) {
		this.entity = entityClass;
	}
	
	public TableBuilder<T> name(String name) {
		this.name = name;
		return this;
	}
	
	public TableBuilder<T> index(boolean unique, IColumnField... fields) {
		if(fields.length==0)
			return this;
		String name = Stream.of(fields).map(field->field.column().columnName()).collect(Collectors.joining("_"));
		Column[] columnList = Stream.of(fields).map(field->field.column()).toArray(Column[]::new);
		Index index = new Index(name, columnList, unique);
		this.indexes = Arrays.copyOf(indexes, indexes.length+1);
		this.indexes[indexes.length-1]=index;
		return this;
	}
	
	public TableBuilder<T> index(Index... indexes) {
		this.indexes = indexes;
		return this;
	}
	
	public TableBuilder<T> column(IColumnField... field) {
		this.columns = (Column[])Stream.of(field).map(IColumnField::column).filter(column->column.table==null).toArray(Column[]::new);
		return this;
	}
	
	public TableBuilder<T> comment(String comment) {
		this.comment = comment;
		return this;
	}
	
	
	public Table<T> create() {
		if(this.name == null)
			this.name = createTableName(entity);
		if(columns==null||columns.length==0) {
			throw new AssertionError("数据库表["+name+"]的字段不能为空");
		}
		return new Table<>(entity, name, indexes, columns, comment);
	}
	
	private static <T extends Entity> String createTableName(Class<T> entityClass) {
		String className = entityClass.getName();
		if(className.indexOf(".entity.")!=-1) {
			String[] strs = className.substring(className.indexOf(".entity.")+".entity.".length()).split("\\.");
			return Str.tranFieldToLineLower(strs[strs.length-2]+strs[strs.length-1].split("$")[0]).substring(0);// 去掉前的"_"
		} else {
			return Str.tranFieldToLineLower(Str.getClazzLastCode(entityClass)).substring(0);// 去掉前的"_"
		}
	}
	
}
