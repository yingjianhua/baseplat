package com.irille.core.repository.query;

import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SqlQuery extends AbstractQuery{
	
	public static final Log LOG = new Log(SqlQuery.class);
	
	public SqlQuery(String sql, Serializable... params) {
		this.sql = sql;
		this.params = params;
	}
	private String sql;
	private Serializable[] params;
	
	protected int start = 0;
	protected int limit = 0;
	protected boolean lock  = false;
	protected Boolean debug = null;
	
	public SqlQuery lock() {
		this.lock = true;
		return this;
	}
	public SqlQuery lock(boolean lock) {
		this.lock = lock;
		return this;
	}
	public SqlQuery debug() {
		this.debug = true;
		return this;
	}
	public SqlQuery debug(boolean debug) {
		this.debug = debug;
		return this;
	}
	public SqlQuery limit(int start, int limit) {
		this.start = start;
		this.limit = limit;
		return this;
	}

	public <T extends Bean<?, ?>> T query(Class<T> beanClass) {
		return super.queryBean(beanClass);
	}
	public <T extends Bean<?, ?>> List<T> queryList(Class<T> beanClass) {
		return super.queryBeans(beanClass);
	}
	public <T extends Object> T queryObject(Class<T> resultClass) {
		return super.queryObject(resultClass);
	}
	public <T extends Object> List<T> queryObjects(Class<T> resultClass) {
		return super.queryObjects(resultClass);
	}
	public Integer queryCount() {
		return super.countRecord();
	}
	public Map<String, Object> queryMap() {
		return super.queryMap();
	}
	public List<Map<String, Object>> queryMaps() {
		return super.queryMaps();
	}
	public int executeUpdate() {
		return super.executeUpdate();
	}
	
	@Override
	protected Serializable[] getParams() {
		return params;
	}

	@Override
	protected String getSql() {
		return BeanBase.getPageSql(this.sql, this.lock, 0, 0);
	}
	
	@Override
	protected Boolean needDebug() {
		if(this.debug!=null)
			return this.debug;
		else
			return super.needDebug();
	}
}
