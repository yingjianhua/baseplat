package com.irille.core.repository.sql;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.orm.IColumnField;
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
	  return mybatisSQL.DELETE_FROM(Entity.table(entityClass).name());
  }
  public <T extends Entity> EntitySQL UPDATE(Class<T> entityClass) {
	  return mybatisSQL.UPDATE(Entity.table(entityClass).name());
  }
  public <T extends Entity> EntitySQL SET(IColumnField field, Serializable param) {
	  mybatisSQL.SET(field.columnName()+"=?");
	  if(param instanceof IEnumOpt) {
		  IEnumOpt opt = (IEnumOpt)param;
		  mybatisSQL.PARAM(opt.getLine().getKey());
	  } else {
		  mybatisSQL.PARAM(param);
	  }
	  return mybatisSQL.getSelf();
  }
  public <T extends Entity> EntitySQL INSERT_INTO(Class<T> entityClass) {
	  return mybatisSQL.INSERT_INTO(Entity.table(entityClass).name());
  }
  public <T extends Entity> EntitySQL VALUES(IColumnField field, Serializable param) {
	  mybatisSQL.VALUES(field.columnName(), "?");
	  if(param instanceof IEnumOpt) {
		  IEnumOpt opt = (IEnumOpt)param;
		  mybatisSQL.PARAM(opt.getLine().getKey());
	  } else {
		  mybatisSQL.PARAM(param);
	  }
	  return mybatisSQL.getSelf();
  }
  public <T extends Entity> EntitySQL SELECT(Class<T> entityClass) {
	  return SELECT(Entity.fields(entityClass));
  }
  public EntitySQL SELECT(String... columns) {
	  return mybatisSQL.SELECT(columns);
  }
  public EntitySQL SELECT(IColumnField fld, String alias) {
	  return mybatisSQL.SELECT(fld.columnFullName()+" as "+alias);
  }
  public EntitySQL SELECT(IColumnField... flds) {
	  return mybatisSQL.SELECT(Stream.of(flds).map(fld->fld.columnNameWithAlias()).toArray(String[]::new));
  }
  
  public <T extends Entity> EntitySQL FROM(Class<T> entityClass) {
	  return mybatisSQL.FROM(Entity.table(entityClass).nameWithAlias());
  }
  
  public <T extends Entity> EntitySQL LEFT_JOIN(Class<T> entityClass, IColumnField fld1, IColumnField fld2) {
	  return mybatisSQL.LEFT_OUTER_JOIN(Entity.table(entityClass).nameWithAlias()+" ON "+fld1.columnFullName()+"="+fld2.columnFullName());
  }
  public <T extends Entity> EntitySQL INNER_JOIN(Class<T> entityClass, IColumnField fld1, IColumnField fld2) {
	  return mybatisSQL.INNER_JOIN(Entity.table(entityClass).nameWithAlias()+" ON "+fld1.columnFullName()+"="+fld2.columnFullName());
  }
  public <T extends Entity> EntitySQL WHERE(Predicate... predicates) {
	  for (Predicate predicate : predicates) {
		  mybatisSQL.WHERE((mybatisSQL.isSelect()?predicate.columnFullName():predicate.columnName())+" "+predicate.getConditions());
		  for(Serializable param:predicate.getParams()) {
			  if(param instanceof IEnumOpt) {
				  IEnumOpt opt = (IEnumOpt)param;
				  mybatisSQL.PARAM(opt.getLine().getKey());
			  } else {
				  mybatisSQL.PARAM(param);
			  }
		  }
	  }
	  return mybatisSQL.getSelf();
  }
  public <T extends Entity> EntitySQL WHERE(IColumnField fld, String conditions) {
	return mybatisSQL.WHERE((mybatisSQL.isSelect()?fld.columnFullName():fld.columnName())+" "+conditions);
  }
  public <T extends Entity> EntitySQL WHERE(IColumnField fld, String conditions, Serializable... params) {
	  WHERE(fld, conditions);
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
  public <T extends Entity> EntitySQL OR() {
	  return mybatisSQL.OR();
  }
  public <T extends Entity> EntitySQL ORDER_BY(IColumnField fld, String type) {
	  return mybatisSQL.ORDER_BY(fld.columnFullName()+" "+type);
  }
  
  public <T extends Entity> EntitySQL GROUP_BY(IColumnField fld) {
	  return mybatisSQL.GROUP_BY(fld.columnFullName());
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
	  //return mybatisSQL.columnLabelWithAlias(fld)+"->"+key+" as "+mybatisSQL.fld.columnName();//老版本mysql不支持的语法
	  return "JSON_EXTRACT("+fld.columnFullName()+", \"$."+key+"\") as "+fld.fieldName();
  }
  public String JSON_UNQUOTE(IColumnField fld, String key) {
	  return "JSON_UNQUOTE(JSON_EXTRACT("+fld.columnFullName()+", \"$."+key+"\")) as "+fld.fieldName();
  }
  public int getStart() {
	  return mybatisSQL.getStart();
  }
  public int getLimit() {
	  return mybatisSQL.getLimit();
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
	
	public int getStart() {
		return start;
	}

	public int getLimit() {
		return limit;
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
  }
}
