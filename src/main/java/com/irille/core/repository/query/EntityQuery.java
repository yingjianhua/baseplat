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
	
	public <R extends Entity> EntityQuery<R> SELECT(Class<R> entityClass) {
		EntityQuery<R> entityQuery = (EntityQuery<R>)this;
		entityQuery.entityClass = entityClass;
		entityQuery.sql.SELECT(entityClass);
		return entityQuery;
	}
	
	public EntityQuery<?> SELECT(IPredicate... predicates) {
		sql.SELECT(predicates);
		return this;
	}
	
	public <R extends Entity> EntityQuery<R> UPDATE(Class<R> entityClass) {
		EntityQuery<R> entityQuery = (EntityQuery<R>)this;
		entityQuery.entityClass = entityClass;
		entityQuery.sql = new EntitySQL().UPDATE(entityClass);
		return entityQuery;
	}
	
	public <R extends Entity> EntityQuery<R> DELETE(Class<R> entityClass) {
		EntityQuery<R> entityQuery = (EntityQuery<R>)this;
		entityQuery.entityClass = entityClass;
		entityQuery.sql = new EntitySQL().DELETE_FROM(entityClass);
		return entityQuery;
	}
	public <R extends Entity> EntityQuery<R> INSERT(Class<R> entityClass) {
		EntityQuery<R> entityQuery = (EntityQuery<R>)this;
		entityQuery.entityClass = entityClass;
		entityQuery.sql = new EntitySQL().INSERT_INTO(entityClass);
		return entityQuery;
	}
	
	public EntityQuery<T> lock() {
		this.sql.LOCK(true);
		return this;
	}
	public EntityQuery<T> lock(boolean lock) {
		this.sql.LOCK(lock);
		return this;
	}
	public <R extends Entity> EntityQuery<R> FROM(Class<R> entityClass) {
		EntityQuery<R> entityQuery = (EntityQuery<R>)this;
		entityQuery.sql.FROM(entityClass);
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
		this.sql.LIMIT(start, limit);
		return this;
	}
	public EntityQuery<T> SET(IPredicate predicate, Serializable param) {
		this.sql.SET(predicate, param);
		return this;
	}
	public EntityQuery<T> VALUES(IPredicate predicate) {
		sql.VALUES(predicate);
		return this;
	}
	public EntityQuery<T> VALUES(IPredicate... predicates) {
		sql.VALUES(predicates);
		return this;
	}
	public EntityQuery<T> WHERE(IPredicate predicate, String conditions, Serializable... params) {
		sql.WHERE(predicate, conditions, params);
		return this;
	}
	public EntityQuery<T> WHERE(IPredicate... predicates) {
		sql.WHERE(predicates);
		return this;
	}
	public EntityQuery<T> WHERE(boolean test, IPredicate predicate, String conditions, Serializable... params) {
		if(test)
			sql.WHERE(predicate, conditions, params);
		return this;
	}
	public EntityQuery<T> WHERE(boolean test, IPredicate predicate, String conditions, Supplier<Serializable> params) {
		if(test)
			sql.WHERE(predicate, conditions, params.get());
		return this;
	}
	public EntityQuery<T> WHERE(boolean test, IPredicate... predicates) {
		if(test)
			sql.WHERE(predicates);
		return this;
	}
	public EntityQuery<T> WHERE(String conditions, Serializable... params) {
		sql.WHERE(conditions, params);
		return this;
	}
	public EntityQuery<T> OR() {
		sql.OR();
		return this;
	}
	public <T2 extends Entity> EntityQuery<T> LEFT_JOIN(Class<T2> entityClass, IPredicate predicate1, IPredicate predicate2) {
		sql.LEFT_JOIN(entityClass, predicate1, predicate2);
		return this;
	}
	public <T2 extends Entity> EntityQuery<T> INNER_JOIN(Class<T2> entityClass, IPredicate predicate1, IPredicate predicate2) {
		sql.INNER_JOIN(entityClass, predicate1, predicate2);
		return this;
	}
	public EntityQuery<T> GROUP_BY(IPredicate predicate) {
		sql.GROUP_BY(predicate);
		return this;
	}
	public EntityQuery<T> GROUP_BY(IPredicate... predicates) {
		sql.GROUP_BY(predicates);
		return this;
	}
	public EntityQuery<T> ORDER_BY(IPredicate predicate) {
		sql.ORDER_BY(predicate);
		return this;
	}
	public EntityQuery<T> ORDER_BY(IPredicate... predicates) {
		sql.ORDER_BY(predicates);
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
		return new Page<>(items, this.sql.getStart(), this.sql.getLimit(), queryCount());
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
		return new Page<>(items, this.sql.getStart(), this.sql.getLimit(), queryCount());
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
	public int executeUpdate() {
		return super.executeUpdate();
	}
	public Serializable executeUpdateReturnGeneratedKey(ColumnTypes type) {
		return super.executeUpdateReturnGeneratedKey(type);
	}
	
	@Override
	protected Serializable[] getParams() {
		return this.sql.PARAMS().toArray(new Serializable[this.sql.PARAMS().size()]);
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
