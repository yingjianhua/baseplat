package com.irille.core.repository.query;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.irille.core.repository.sql.I18NSQL;
import com.irille.core.repository.sql.SQL;

import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanMain;
import irille.pub.tb.FldLanguage.Language;
import irille.pub.tb.IEnumFld;

public class BeanQuery<T> extends AbstractQuery {
	public static final Log LOG = new Log(BeanQuery.class);
	
	protected Class<T> beanClass;
	protected SQL sql;
	
	protected Boolean debug = null;

	public BeanQuery() {
		this.sql = new SQL();
	}
	public BeanQuery(Language lang) {
		this.sql = new I18NSQL(lang);
	}
	
	public <R extends BeanMain<?, ?>> BeanQuery<R> SELECT(Class<R> beanClass) {
		BeanQuery<R> beanQuery = (BeanQuery<R>)this;
		beanQuery.beanClass = beanClass;
		beanQuery.sql.SELECT(beanClass);
		return beanQuery;
	}
	
	public BeanQuery<?> SELECT(IEnumFld... flds) {
		sql.SELECT(flds);
		return this;
	}
	
	public BeanQuery<?> SELECT(IEnumFld fld, String alias) {
		sql.SELECT(fld, alias);
		return this;
	}
	
	public <R extends BeanMain<?, ?>> BeanQuery<R> UPDATE(Class<R> beanClass) {
		BeanQuery<R> beanQuery = (BeanQuery<R>)this;
		beanQuery.beanClass = beanClass;
		beanQuery.sql = new SQL().UPDATE(beanClass);
		return beanQuery;
	}
	
	public <R extends BeanMain<?, ?>> BeanQuery<R> DELETE(Class<R> beanClass) {
		BeanQuery<R> beanQuery = (BeanQuery<R>)this;
		beanQuery.beanClass = beanClass;
		beanQuery.sql = new SQL().DELETE_FROM(beanClass);
		return beanQuery;
	}
	
	public BeanQuery<T> lock() {
		this.sql.LOCK(true);
		return this;
	}
	public BeanQuery<T> lock(boolean lock) {
		this.sql.LOCK(lock);
		return this;
	}
	public <R extends BeanMain<?, ?>> BeanQuery<R> FROM(Class<R> beanClass) {
		BeanQuery<R> beanQuery = (BeanQuery<R>)this;
		beanQuery.sql.FROM(beanClass);
		beanQuery.beanClass = beanClass;
		return beanQuery;
	}
	public BeanQuery<T> debug() {
		this.debug = true;
		return this;
	}
	public BeanQuery<T> debug(boolean debug) {
		this.debug = debug;
		return this;
	}
	public BeanQuery<T> limit(int start, int limit) {
		this.sql.LIMIT(start, limit);
		return this;
	}
	public BeanQuery<T> SET(IEnumFld fld, Serializable param) {
		sql.SET(fld, param);
		return this;
	}
	public BeanQuery<T> WHERE(IEnumFld fld, String conditions, Serializable... params) {
		sql.WHERE(fld, conditions, params);
		return this;
	}
	public BeanQuery<T> WHERE(boolean test, IEnumFld fld, String conditions, Serializable... params) {
		if(test)
			sql.WHERE(fld, conditions, params);
		return this;
	}
	public BeanQuery<T> WHERE(boolean test, IEnumFld fld, String conditions, Supplier<Serializable> params) {
		if(test)
			sql.WHERE(fld, conditions, params.get());
		return this;
	}
	public BeanQuery<T> WHERE(String conditions, Serializable... params) {
		sql.WHERE(conditions, params);
		return this;
	}
	public <T2 extends BeanMain<?, ?>> BeanQuery<T> LEFT_JOIN(Class<T2> beanClass, IEnumFld fld1, IEnumFld fld2) {
		sql.LEFT_JOIN(beanClass, fld1, fld2);
		return this;
	}
	public BeanQuery<T> GROUP_BY(IEnumFld fld) {
		sql.GROUP_BY(fld);
		return this;
	}
	public BeanQuery<T> ORDER_BY(IEnumFld fld, String type) {
		sql.ORDER_BY(fld, type);
		return this;
	}
	
	/**
	 * 通过pkey查询bean对象
	 * @author yingjianhua
	 */
	@SuppressWarnings("unchecked")
	public <R extends Object> R query(Class<R> resultClass) {
		if(BeanMain.class.isAssignableFrom(resultClass)) {
			return (R)queryBean((Class<Bean<?, ?>>)resultClass);
		} else {
			return super.queryObject(resultClass);
		}
	}
	@SuppressWarnings("unchecked")
	public <R extends Object> List<R> queryList(Class<R> resultClass) {
		if(BeanMain.class.isAssignableFrom(resultClass)) {
			return (List<R>)queryBeans((Class<Bean<?, ?>>)resultClass);
		} else {
			return super.queryObjects(resultClass);
		}
	}
	@SuppressWarnings("unchecked")
	public T query() {
		if(Bean.class.isAssignableFrom(this.beanClass)) {
			return (T) this.queryBean((Class<Bean<?, ?>>)beanClass);
		} else{
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public List<T> queryList() {
		if(Bean.class.isAssignableFrom(this.beanClass)) {
			return (List<T>) this.queryBeans((Class<Bean<?, ?>>)beanClass);
		} else{
			return null;
		}
	}
	@Deprecated
	public <R extends Object> R queryObject(Class<R> resultClass) {
		return super.queryObject(resultClass);
	}
	@Deprecated
	public <R extends Object> List<R> queryObjects(Class<R> resultClass) {
		return super.queryObjects(resultClass);
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
	public int executeUpdate() {
		return super.executeUpdate();
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
