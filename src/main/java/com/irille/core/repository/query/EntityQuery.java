package com.irille.core.repository.query;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.irille.core.commons.SetBeans.SetBean.SetBeans;
import com.irille.core.repository.orm.ColumnTypes;
import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.orm.IColumnField;
import com.irille.core.repository.sql.EntitySQL;
import com.irille.core.repository.sql.I18NEntitySQL;

import irille.pub.Log;
import irille.pub.tb.FldLanguage.Language;
import irille.view.BaseView;

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
	
	public EntityQuery<?> SELECT(IColumnField... flds) {
		sql.SELECT(flds);
		return this;
	}
	public EntityQuery<?> SELECT(IColumnField fld, String alias) {
		sql.SELECT(fld, alias);
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
	public EntityQuery<T> SET(IColumnField fld, Serializable param) {
		this.sql.SET(fld, param);
		return this;
	}
	public EntityQuery<T> VALUES(IColumnField field, Serializable param) {
		sql.VALUES(field, param);
		return this;
	}
	public EntityQuery<T> WHERE(IColumnField fld, String conditions, Serializable... params) {
		sql.WHERE(fld, conditions, params);
		return this;
	}
	public EntityQuery<T> WHERE(boolean test, IColumnField fld, String conditions, Serializable... params) {
		if(test)
			sql.WHERE(fld, conditions, params);
		return this;
	}
	public EntityQuery<T> WHERE(boolean test, IColumnField fld, String conditions, Supplier<Serializable> params) {
		if(test)
			sql.WHERE(fld, conditions, params.get());
		return this;
	}
	public EntityQuery<T> WHERE(String conditions, Serializable... params) {
		sql.WHERE(conditions, params);
		return this;
	}
	public <T2 extends Entity> EntityQuery<T> LEFT_JOIN(Class<T2> entityClass, IColumnField fld1, IColumnField fld2) {
		sql.LEFT_JOIN(entityClass, fld1, fld2);
		return this;
	}
	public <T2 extends Entity> EntityQuery<T> INNER_JOIN(Class<T2> entityClass, IColumnField fld1, IColumnField fld2) {
		sql.INNER_JOIN(entityClass, fld1, fld2);
		return this;
	}
	public EntityQuery<T> GROUP_BY(IColumnField fld) {
		sql.GROUP_BY(fld);
		return this;
	}
	public EntityQuery<T> ORDER_BY(IColumnField fld, String type) {
		sql.ORDER_BY(fld, type);
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
//	@Deprecated
//	public <R extends Object> R queryObject(Class<R> resultClass) {
//		return super.queryObject(resultClass);
//	}
//	@Deprecated
//	public <R extends Object> List<R> queryObjects(Class<R> resultClass) {
//		return super.queryObjects(resultClass);
//	}
	public Map<String, Object> queryMap() {
		return super.queryMap();
	}
	public List<Map<String, Object>> queryMaps() {
		return super.queryMaps();
	}
	public Integer queryCount() {
		return countRecord();
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
