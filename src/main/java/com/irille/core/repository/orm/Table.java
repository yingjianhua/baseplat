package com.irille.core.repository.orm;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.irille.core.controller.JsonWriter;
import com.irille.core.repository.Query;
import com.irille.core.repository.db.ConnectionManager;

public class Table <T extends Entity> {
	
	private static final Logger logger = LoggerFactory.getLogger(Table.class);
	
	private Class<T> entity;
	private String name;
	private String catalog;//数据库名
	private Index[] indexes;
	private Column primaryKey;
	private Column[] columns;
	private String comment;//描述
	
	Table(Class<T> entityClass, String name, Index[] indexes, Column[] columns, String comment) {
		Stream.of(columns).forEach(column->{
			column.table=this;
			if(column.primary)
				this.primaryKey=column;
		});
		this.entity = entityClass;
		this.name = name;
		this.indexes = indexes;
		this.columns = columns;
		this.comment = comment;
	}
	
	public Class<T> entity() {
		return entity;
	}
	public String catalog() {
		return catalog;
	}
	public String name() {
		return name;
	}
	public String nameWithAlias() {
		return name+" "+entity.getSimpleName();
	}
	public Index[] indexes() {
		return Arrays.copyOf(indexes, indexes.length);
	}
	public Column primaryKey() {
		return primaryKey;
	}
	public Column[] columns() {
		return Arrays.copyOf(columns, columns.length);
	}
	public String comment() {
		return comment;
	}
	
	public static void main(String[] args) throws SQLException {
		System.out.println(Query.sql("select count(0) from social_twitter_user").query(Integer.class));
	}
	
	public boolean exists() throws SQLException {
		Connection connection = ConnectionManager.getConnection();
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet rs = meta.getTables(connection.getCatalog(), null, name, null);
		return rs.next()?true:false;
	}
	
	public boolean haveRecord() {
		return Query.sql("select count(0) from "+name).query(Integer.class)>0?true:false;
	}
	
	public void drop(boolean force) throws SQLException {
		if(exists() && (force||!haveRecord())) {
			Query.sql("DROP TABLE IF EXISTS "+name).executeUpdate();
			logger.info("删除表[{}]-->成功!", name);
		}
	}
	
	private String createSql() {
		StringBuilder s = new StringBuilder();
		s.append("create table if not exists `"+name+"` (");
		Column primary = null;
		for(Column column:columns) {
			s.append(column.createSql()).append(",");
			if(column.isPrimary())
				primary = column;
		}
		if(primary!=null)
			s.append("primary key (`").append(primary.columnName).append("`),");
		for(Index index:indexes) {
			s.append(index.createSql()).append(",");
		}
		s.deleteCharAt(s.lastIndexOf(","));
		s.append(")").append("ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
		if(comment!=null&&!"".equals(comment))
			s.append(" comment=`").append(comment).append("`");
		return s.toString();
	}
	
	public void create() throws SQLException {
		if(exists()) {
			logger.info("表[{}]已存在!", name);
		} else {
			Query.sql(createSql()).executeUpdate();
			logger.info("建表[{}]-->成功!", name);
		}
	}

}
