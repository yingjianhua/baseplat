package com.irille.core.repository.query;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.irille.core.commons.SetBeans.SetBean.SetBeans;
import com.irille.core.repository.orm.ColumnTypes;
import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.sql.EntitySQL;
import com.irille.core.repository.sql.I18NEntitySQL;

import irille.pub.Log;
import irille.pub.tb.FldLanguage.Language;
import irille.view.BaseView;
import irille.view.Page;

public class EntityQuery<T> extends AbstractQuery {
	public static final Log LOG = new Log(EntityQuery.class);
	
	protected Class<T> entityClass;
	protected EntitySQL sql;
	
	protected Boolean debug = null;

	public EntityQuery() {
		this.sql = new EntitySQL();
	}
	public EntityQuery(Language lang) {
		this.sql = new I18NEntitySQL(lang);
	}
	
	public <R extends Entity> EntityQuery<?> select(Class<R> entityClass) {
		this.sql.select(entityClass);
		return this;
	}
	
	public EntityQuery<?> select(IPredicate... predicates) {
		sql.select(predicates);
		return this;
	}
	
	public <R extends Entity> EntityQuery<R> update(Class<R> entityClass) {
		EntityQuery<R> entityQuery = (EntityQuery<R>)this;
		entityQuery.entityClass = entityClass;
		entityQuery.sql = new EntitySQL().update(entityClass);
		return entityQuery;
	}
	
	public <R extends Entity> EntityQuery<R> delete(Class<R> entityClass) {
		EntityQuery<R> entityQuery = (EntityQuery<R>)this;
		entityQuery.entityClass = entityClass;
		entityQuery.sql = new EntitySQL().deleteFrom(entityClass);
		return entityQuery;
	}
	public <R extends Entity> EntityQuery<R> insert(Class<R> entityClass) {
		EntityQuery<R> entityQuery = (EntityQuery<R>)this;
		entityQuery.entityClass = entityClass;
		entityQuery.sql = new EntitySQL().insertInto(entityClass);
		return entityQuery;
	}
	
	public EntityQuery<T> lock() {
		this.sql.lock(true);
		return this;
	}
	public EntityQuery<T> lock(boolean lock) {
		this.sql.lock(lock);
		return this;
	}
	public <R extends Entity> EntityQuery<R> FROM(Class<R> entityClass) {
		return from(entityClass);
	}
	public <R extends Entity> EntityQuery<R> from(Class<R> entityClass) {
		EntityQuery<R> entityQuery = (EntityQuery<R>)this;
		entityQuery.sql.from(entityClass);
		entityQuery.entityClass = entityClass;
		return entityQuery;
	}
	public EntityQuery<T> debug() {
		this.debug = true;
		return this;
	}
	public EntityQuery<T> debug(boolean debug) {
		this.debug = debug;
		return this;
	}
	public EntityQuery<T> limit(int start, int limit) {
		this.sql.limit(start, limit);
		return this;
	}
	public EntityQuery<T> set(IPredicate predicate, Serializable param) {
		this.sql.set(predicate, param);
		return this;
	}
	public EntityQuery<T> values(IPredicate predicate) {
		sql.values(predicate);
		return this;
	}
	public EntityQuery<T> values(IPredicate... predicates) {
		sql.values(predicates);
		return this;
	}
	public EntityQuery<T> where(IPredicate predicate, String conditions, Serializable... params) {
		sql.where(predicate, conditions, params);
		return this;
	}
	public EntityQuery<T> where(IPredicate... predicates) {
		sql.where(predicates);
		return this;
	}
	public EntityQuery<T> where(boolean test, IPredicate predicate, String conditions, Serializable... params) {
		if(test)
			sql.where(predicate, conditions, params);
		return this;
	}
	public EntityQuery<T> where(boolean test, IPredicate predicate, String conditions, Supplier<Serializable> params) {
		if(test)
			sql.where(predicate, conditions, params.get());
		return this;
	}
	public EntityQuery<T> where(boolean test, IPredicate... predicates) {
		if(test)
			sql.where(predicates);
		return this;
	}
	public EntityQuery<T> where(String conditions, Serializable... params) {
		sql.where(conditions, params);
		return this;
	}
	public EntityQuery<T> or() {
		sql.or();
		return this;
	}
	public <T2 extends Entity> EntityQuery<T> leftJoin(Class<T2> entityClass, IPredicate predicate1, IPredicate predicate2) {
		sql.leftJoin(entityClass, predicate1, predicate2);
		return this;
	}
	public <T2 extends Entity> EntityQuery<T> innerJoin(Class<T2> entityClass, IPredicate predicate1, IPredicate predicate2) {
		sql.innerJoin(entityClass, predicate1, predicate2);
		return this;
	}
	public EntityQuery<T> groupBy(IPredicate predicate) {
		sql.groupBy(predicate);
		return this;
	}
	public EntityQuery<T> groupBy(IPredicate... predicates) {
		sql.groupBy(predicates);
		return this;
	}
	public EntityQuery<T> orderBy(IPredicate predicate) {
		sql.orderBy(predicate);
		return this;
	}
	public EntityQuery<T> orderBy(IPredicate... predicates) {
		sql.orderBy(predicates);
		return this;
	}
	
	/**
	 * 通过pkey查询Entity对象
	 * @author yingjianhua
	 */
	@SuppressWarnings("unchecked")
	public <R extends Object> R query(Class<R> resultClass) {
		if(Entity.class.isAssignableFrom(resultClass)) {
			return (R)queryEntity((Class<Entity>)resultClass);
		} else if(BaseView.class.isAssignableFrom(resultClass)){
			return SetBeans.set(queryMap(), resultClass);
		} else {
			return super.queryObject(resultClass);
		}
	}
	@SuppressWarnings("unchecked")
	public <R extends Object> List<R> queryList(Class<R> resultClass) {
		if(Entity.class.isAssignableFrom(resultClass)) {
			return (List<R>)queryEntitys((Class<Entity>)resultClass);
		} else if(BaseView.class.isAssignableFrom(resultClass)){
			return SetBeans.setList(queryMaps(), resultClass);
		} else {
			return super.queryObjects(resultClass);
		}
	}
	@SuppressWarnings("unchecked")
	public <R extends Object> Page<R> queryPage(Class<R> resultClass) {
		List<R> items = null;
		if(Entity.class.isAssignableFrom(resultClass)) {
			items = (List<R>)queryEntitys((Class<Entity>)resultClass);
		} else if(BaseView.class.isAssignableFrom(resultClass)){
			items = SetBeans.setList(queryMaps(), resultClass);
		} else {
			items = super.queryObjects(resultClass);
		}
		return new Page<>(items, queryCount());
	}
	@SuppressWarnings("unchecked")
	public T query() {
		if(Entity.class.isAssignableFrom(this.entityClass)) {
			return (T) this.queryEntity((Class<Entity>)entityClass);
		} else{
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public List<T> queryList() {
		if(Entity.class.isAssignableFrom(this.entityClass)) {
			return (List<T>) this.queryEntitys((Class<Entity>)entityClass);
		} else{
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public Page<T> queryPage() {
		List<T> items = null;
		if(Entity.class.isAssignableFrom(this.entityClass)) {
			items = (List<T>) this.queryEntitys((Class<Entity>)entityClass);
		} else{
			return null;
		}
		return new Page<>(items, queryCount());
	}
	public Map<String, Object> queryMap() {
		return super.queryMap();
	}
	public List<Map<String, Object>> queryMaps() {
		return super.queryMaps();
	}
	public Integer queryCount() {
		return countRecord();
	}
	public boolean exists() {
		return countRecord()>0;
	}
	public int execute() {
		return super.executeUpdate();
	}
	public Serializable executeUpdateReturnGeneratedKey(ColumnTypes type) {
		return super.executeUpdateReturnGeneratedKey(type);
	}
	public Serializable executeFetchGeneratedKey(ColumnTypes type) {
		return super.executeUpdateReturnGeneratedKey(type);
	}
	
	@Override
	protected Serializable[] getParams() {
		return this.sql.params().toArray(new Serializable[this.sql.params().size()]);
	}

	@Override
	protected String getSql() {
		return this.sql.toString();
	}
	
	@Override
	protected Boolean needDebug() {
		if(this.debug!=null)
			return this.debug;
		else
			return super.needDebug();
	}
	
}
