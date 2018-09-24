package com.irille.core.repository.sql;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.orm.IColumnField;
import com.irille.core.repository.orm.Table;
import com.irille.core.repository.query.Predicate;

import irille.pub.tb.IEnumOpt;


/**
 * from mybatis version 3.4.6
 * @author yingjianhua
 *
 */
public class EntitySQL {

  private MybatisSQL mybatisSQL;
  
  public EntitySQL() {
	  mybatisSQL = new MybatisSQL(); 
  }
  public <T extends Entity> EntitySQL DELETE_FROM(Class<T> entityClass) {
	  return mybatisSQL.DELETE_FROM(Entity.table(entityClass));
  }
  public <T extends Entity> EntitySQL UPDATE(Class<T> entityClass) {
	  return mybatisSQL.UPDATE(Entity.table(entityClass));
  }
  public <T extends Entity> EntitySQL SET(IColumnField fld, Serializable param) {
	  return SET(fld.column(), param);
  }
  public <T extends Entity> EntitySQL SET(Column column, Serializable param) {
	  mybatisSQL.SET(column);
	  if(param instanceof IEnumOpt) {
		  IEnumOpt opt = (IEnumOpt)param;
		  mybatisSQL.PARAM(opt.getLine().getKey());
	  } else {
		  mybatisSQL.PARAM(param);
	  }
	  return mybatisSQL.getSelf();
  }
  public <T extends Entity> EntitySQL INSERT_INTO(Class<T> entityClass) {
	  return mybatisSQL.INSERT_INTO(Entity.table(entityClass));
  }
  public <T extends Entity> EntitySQL VALUES(Column column, Serializable param) {
	  mybatisSQL.VALUES(column);
	  if(param instanceof IEnumOpt) {
		  IEnumOpt opt = (IEnumOpt)param;
		  mybatisSQL.PARAM(opt.getLine().getKey());
	  } else {
		  mybatisSQL.PARAM(param);
	  }
	  return mybatisSQL.getSelf();
  }
  public <T extends Entity> EntitySQL SELECT(Class<T> entityClass) {
	  return mybatisSQL.SELECT(Entity.table(entityClass));
  }
  public EntitySQL SELECT(String... columns) {
	  return mybatisSQL.SELECT(columns);
  }
  public EntitySQL SELECT(IColumnField fld, String alias) {
	  return mybatisSQL.SELECT(fld.column(), alias);
  }
  public EntitySQL SELECT(Predicate... predicate) {
	  return mybatisSQL.SELECT(fld.column(), alias);
  }
  
  public EntitySQL SELECT(IColumnField... flds) {
	  return mybatisSQL.SELECT(Stream.of(flds).map(fld->fld.column()).toArray(Column[]::new));
  }
  
  public <T extends Entity> EntitySQL FROM(Class<T> entityClass) {
	  return mybatisSQL.FROM(Entity.table(entityClass));
  }
  
  public <T extends Entity> EntitySQL LEFT_JOIN(Class<T> entityClass, IColumnField fld1, IColumnField fld2) {
	  return mybatisSQL.LEFT_OUTER_JOIN(Entity.table(entityClass), fld1.column(), fld2.column());
  }
  public <T extends Entity> EntitySQL INNER_JOIN(Class<T> entityClass, IColumnField fld1, IColumnField fld2) {
	  return mybatisSQL.INNER_JOIN(Entity.table(entityClass), fld1.column(), fld2.column());
  }
  
  public <T extends Entity> EntitySQL WHERE(IColumnField fld, String conditions) {
	  return mybatisSQL.WHERE(fld.column(), conditions);
  }
  
  public <T extends Entity> EntitySQL WHERE(IColumnField fld, String conditions, Serializable... params) {
	  return WHERE(fld.column(), conditions, params);
  }
  public <T extends Entity> EntitySQL WHERE(Column column, String conditions, Serializable... params) {
	  mybatisSQL.WHERE(column, conditions);
	  for(Serializable param:params) {
		  if(param instanceof IEnumOpt) {
			  IEnumOpt opt = (IEnumOpt)param;
			  mybatisSQL.PARAM(opt.getLine().getKey());
		  } else {
			  mybatisSQL.PARAM(param);
		  }
	  }
	  return mybatisSQL.getSelf();
  }
  
  public <T extends Entity> EntitySQL WHERE(String conditions, Serializable... params) {
	  mybatisSQL.WHERE(conditions);
	  for(Serializable param:params) {
		  if(param instanceof IEnumOpt) {
			  IEnumOpt opt = (IEnumOpt)param;
			  mybatisSQL.PARAM(opt.getLine().getKey());
		  } else {
			  mybatisSQL.PARAM(param);
		  }
	  }
	  return mybatisSQL.getSelf();
  }
  
  public <T extends Entity> EntitySQL ORDER_BY(IColumnField fld, String type) {
	  return mybatisSQL.ORDER_BY(fld.column(), type);
  }
  
  public <T extends Entity> EntitySQL GROUP_BY(IColumnField fld) {
	  return mybatisSQL.GROUP_BY(fld.column());
  }
  
  public <T extends Entity> EntitySQL LIMIT(int start, int limit) {
	  return mybatisSQL.LIMIT(start, limit);
  }
  
  public <T extends Entity> EntitySQL LOCK(boolean lock) {
	  return mybatisSQL.LOCK(lock);
  }
  
  public <T extends Entity> EntitySQL PARAM(Serializable param) {
	  return mybatisSQL.PARAM(param);
  }
  
  public List<Serializable> PARAMS() {
	  return mybatisSQL.PARAMS();
  }
  
  public String JSON_EXTRACT(IColumnField fld, String key) {
	  //return mybatisSQL.columnLabelWithAlias(fld)+"->"+key+" as "+mybatisSQL.fld.column().columnName();//老版本mysql不支持的语法
	  return "JSON_EXTRACT("+mybatisSQL.columnLabelWithAlias(fld.column())+", \"$."+key+"\") as "+fld.column().columnName();
  }
  public String JSON_UNQUOTE(IColumnField fld, String key) {
	  return "JSON_UNQUOTE(JSON_EXTRACT("+mybatisSQL.columnLabelWithAlias(fld.column())+", \"$."+key+"\")) as "+fld.column().columnName();
  }
  
  
  @Override
  public String toString() {
	  return mybatisSQL.toString();
  }
  
  class MybatisSQL extends AbstractSQL<EntitySQL> {

	private int start = 0;
	private int limit = 0;
	private boolean lock  = false;
	
	@Override
	public EntitySQL getSelf() {
		return EntitySQL.this;
	}
	public <T extends Entity> EntitySQL SELECT(Table<?> table) {
		return super.SELECT(table.name()+".*");
	}
	
	public EntitySQL SELECT(String... columns) {
		return super.SELECT(columns);
	}

	public EntitySQL SELECT(Column... columns) {
		return super.SELECT(Stream.of(columns).map(column->this.columnLabelWithAlias(column)).toArray(String[]::new));
	}
	
	public EntitySQL SELECT(Column column, String alias) {
		return super.SELECT(column.columnName()+" as "+alias);
	}
	
	public <T extends Entity> EntitySQL FROM(Table<?> table) {
		return super.FROM(tableNameWithAlias(table));
	}
	
	public <T extends Entity> EntitySQL LEFT_OUTER_JOIN(Table<?> table, Column column1, Column column2) {
		return super.LEFT_OUTER_JOIN(tableNameWithAlias(table)+" ON "+columnLabelWithAlias(column1)+"="+columnLabelWithAlias(column2));
	}
	public <T extends Entity> EntitySQL INNER_JOIN(Table<?> table, Column column1, Column column2) {
		return super.INNER_JOIN(tableNameWithAlias(table)+" ON "+columnLabelWithAlias(column1)+"="+columnLabelWithAlias(column2));
	}
	
	public <T extends Entity> EntitySQL WHERE(Column column, String conditions) {
		if(isSelect())
			return super.WHERE(columnLabelWithAlias(column)+" "+conditions);
		else
			return super.WHERE(column.columnName()+" "+conditions);
	}
	
	public  <T extends Entity> EntitySQL ORDER_BY(Column column, String type) {
	  return super.ORDER_BY(columnLabelWithAlias(column)+" "+type);
	}
	
	public  <T extends Entity> EntitySQL GROUP_BY(Column column) {
	  return super.GROUP_BY(columnLabelWithAlias(column));
	}
	
	public <T extends Entity> EntitySQL DELETE_FROM(Table<?> table) {
		return super.DELETE_FROM(table.name());
	}
	public <T extends Entity> EntitySQL UPDATE(Table<?> table) {
		return super.UPDATE(table.name());
	}
	public EntitySQL SET(Column column) {
		return super.SET(column.columnName()+"=?");
	}
	public <T extends Entity> EntitySQL INSERT_INTO(Table<?> table) {
		return super.INSERT_INTO(table.name());
	}
	public <T extends Entity> EntitySQL VALUES(Column column) {
		return super.VALUES(column.columnName(), "?");
	}
	public <T extends Entity> EntitySQL LIMIT(int start, int limit) {
		this.start = start;
		this.limit = limit;
		return getSelf();
	}
	public <T extends Entity> EntitySQL LOCK(boolean lock) {
		this.lock = lock;
		return getSelf();
	}
	@Override
	public String toString() {
		String sql = super.toString();
		if(isSelect() && limit > 0)
			sql += " LIMIT " + start + "," + limit;
		if(lock)
			sql += " FOR UPDATE";
		return sql;
	}
	
	private <T extends Entity> String tableNameWithAlias(Table<?> table) {
		return table.name()+" "+table.entity().getSimpleName();
	}
	
	private <T extends Entity> String alias(Column column) {
		return column.getTable().entity().getSimpleName();
	}
	
	private String columnLabelWithAlias(Column column) {
		return alias(column)+"."+column.columnName()+" as "+column.fieldName();
//		return alias(column)+"."+column.columnName()+" as "+column.fieldName();
	}
	
  }
}
