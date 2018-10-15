package com.irille.core.repository.sql;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.query.IPredicate;

import irille.pub.bean.BeanMain;
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
  public <T extends Entity> EntitySQL deleteFrom(Class<T> entityClass) {
	  return mybatisSQL.DELETE_FROM(Entity.table(entityClass).name());
  }
  public <T extends Entity> EntitySQL update(Class<T> entityClass) {
	  return mybatisSQL.UPDATE(Entity.table(entityClass).name());
  }
  public <T extends Entity> EntitySQL set(IPredicate predicate, Serializable param) {
	  mybatisSQL.SET(predicate.columnName()+"=?");
	  if(param instanceof IEnumOpt) {
		  IEnumOpt opt = (IEnumOpt)param;
		  mybatisSQL.PARAM(opt.getLine().getKey());
	  } else {
		  mybatisSQL.PARAM(param);
	  }
	  return mybatisSQL.getSelf();
  }
  public <T extends Entity> EntitySQL insertInto(Class<T> entityClass) {
	  return mybatisSQL.INSERT_INTO(Entity.table(entityClass).name());
  }
  public <T extends Entity> EntitySQL values(IPredicate predicate) {
	  mybatisSQL.VALUES(predicate.columnName(), "?");
	  if(predicate.getParams()[0] instanceof IEnumOpt) {
		  IEnumOpt opt = (IEnumOpt)predicate.getParams()[0];
		  mybatisSQL.PARAM(opt.getLine().getKey());
	  } else {
		  mybatisSQL.PARAM(predicate.getParams()[0]);
	  }
	  return mybatisSQL.getSelf();
  }
  public <T extends Entity> EntitySQL values(IPredicate... predicates) {
	  for (IPredicate predicate : predicates)
		  values(predicate);
	  return mybatisSQL.getSelf();
  }
  public <T extends Entity> EntitySQL select(Class<T> entityClass) {
	  return select(Entity.fields(entityClass));
  }
  public EntitySQL select(String... columns) {
	  return mybatisSQL.SELECT(columns);
  }
  public EntitySQL select(IPredicate... predicates) {
	  return mybatisSQL.SELECT(Stream.of(predicates).map(predicate->predicate.columnNameWithAlias()).toArray(String[]::new));
  }
  public EntitySQL select(EntitySQL table, String alias) {
	  if(table.mybatisSQL.PARAMS().size()>0)
		  mybatisSQL.PARAM(table.mybatisSQL.PARAMS().toArray(new Serializable[]{}));
	  return mybatisSQL.SELECT("("+table+") as "+alias);
  }
  public <T extends Entity> EntitySQL from(Class<T> entityClass) {
	  return mybatisSQL.FROM(Entity.table(entityClass).nameWithAlias());
  }
  public <T extends BeanMain<?, ?>> EntitySQL from(EntitySQL table, String alias) {
	  if(table.mybatisSQL.PARAMS().size()>0)
		  mybatisSQL.PARAM(table.mybatisSQL.PARAMS().toArray(new Serializable[]{}));
	  return mybatisSQL.FROM("("+table+") as "+alias);
  }
  public <T extends Entity> EntitySQL leftJoin(Class<T> entityClass, IPredicate predicate1, IPredicate predicate2) {
	  return mybatisSQL.LEFT_OUTER_JOIN(Entity.table(entityClass).nameWithAlias()+" ON "+predicate1.columnFullName()+"="+predicate2.columnFullName());
  }
  public <T extends Entity> EntitySQL innerJoin(Class<T> entityClass, IPredicate predicate1, IPredicate predicate2) {
	  return mybatisSQL.INNER_JOIN(Entity.table(entityClass).nameWithAlias()+" ON "+predicate1.columnFullName()+"="+predicate2.columnFullName());
  }
  public <T extends Entity> EntitySQL where(IPredicate... predicates) {
	  for (IPredicate predicate : predicates) {
		  mybatisSQL.WHERE((mybatisSQL.isSelect()?predicate.columnFullName():predicate.columnName())+" "+predicate.getConditions());
		  if(predicate.getParams()==null)
			  continue;
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
  public <T extends Entity> EntitySQL where(IPredicate predicate, String conditions) {
	return mybatisSQL.WHERE((mybatisSQL.isSelect()?predicate.columnFullName():predicate.columnName())+" "+conditions);
  }
  public <T extends Entity> EntitySQL where(IPredicate predicate, String conditions, Serializable... params) {
	  where(predicate, conditions);
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
  
  public <T extends Entity> EntitySQL where(String conditions, Serializable... params) {
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
  public <T extends Entity> EntitySQL or() {
	  return mybatisSQL.OR();
  }
  public <T extends Entity> EntitySQL orderBy(IPredicate predicate) {
	  return mybatisSQL.ORDER_BY(predicate.columnFullName()+" "+predicate.getConditions());
  }
  public <T extends Entity> EntitySQL orderBy(IPredicate... predicates) {
	  for (IPredicate predicate : predicates)
		  orderBy(predicate);
	  return mybatisSQL.getSelf();
  }
  public <T extends Entity> EntitySQL groupBy(IPredicate predicate) {
	  return mybatisSQL.GROUP_BY(predicate.columnFullName());
  }
  public <T extends Entity> EntitySQL groupBy(IPredicate... predicates) {
	  for (IPredicate predicate : predicates)
		groupBy(predicate);
	  return mybatisSQL.getSelf();
  }
  
  public <T extends Entity> EntitySQL limit(int start, int limit) {
	  return mybatisSQL.LIMIT(start, limit);
  }
  
  public <T extends Entity> EntitySQL lock(boolean lock) {
	  return mybatisSQL.LOCK(lock);
  }
  
  public <T extends Entity> EntitySQL param(Serializable param) {
	  return mybatisSQL.PARAM(param);
  }
  
  public List<Serializable> params() {
	  return mybatisSQL.PARAMS();
  }
  
  public String JSON_EXTRACT(IPredicate predicate, String key) {
	  //return mybatisSQL.columnLabelWithAlias(predicate)+"->"+key+" as "+mybatisSQL.predicate.columnName();//老版本mysql不支持的语法
	  return "JSON_EXTRACT("+predicate.columnFullName()+", \"$."+key+"\") as "+predicate.alias();
  }
  public String JSON_UNQUOTE(IPredicate predicate, String key) {
	  return "JSON_UNQUOTE(JSON_EXTRACT("+predicate.columnFullName()+", \"$."+key+"\")) as "+predicate.alias();
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
