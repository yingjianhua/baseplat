package com.irille.core.repository.query;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.irille.core.repository.db.ConnectionManager;
import com.irille.core.repository.orm.ColumnTypes;
import com.irille.core.repository.orm.Entity;
import com.irille.core.web.config.AppConfig;

import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;

public abstract class AbstractQuery {
	private static final Log LOG = new Log(AbstractQuery.class);
	private static final Logger logger = LoggerFactory.getLogger(AbstractQuery.class);
	
	protected static final Config config = new Config();
	public static final void config(boolean debug) {
		config.debug = debug;
	}
	static class Config {
		protected boolean debug = false;
	}
	protected abstract Serializable[] getParams();
	protected abstract String getSql();
	protected Boolean needDebug() {
		return config.debug;
	};
	/**
	 * @return
	 * @author yingjianhua
	 */
	protected Map<String, Object> queryMap() {
		return query(rs->ResultMapper.asMap(rs));
	}
	
	/**
	 * @return
	 * @author yingjianhua
	 */
	protected List<Map<String, Object>> queryMaps() {
		return query(rs->ResultMapper.asMaps(rs));
	}
	/**
	 * 
	 * @author yingjianhua
	 */
	protected <T extends Object> T queryObject(Class<T> resultClass) {
		return query(rs->ResultMapper.asObject(rs, resultClass));
	}
	/**
	 * 
	 * @author yingjianhua
	 */
	protected <T extends Object> List<T> queryObjects(Class<T> resultClass) {
		return query(rs->ResultMapper.asObjects(rs, resultClass));
	}
	
	/**
	 * 统计记录数
	 * 
	 * @return
	 * @author yingjianhua
	 */
	protected Integer countRecord() {
		String sql = getSql();
		int s = sql.indexOf(" LIMIT");
		if(s!=-1)
			sql = sql.substring(0, s);
		sql = sql.replaceFirst("(select|SELECT)\\s+.*?\\s+(FROM|from)", "SELECT COUNT(1) FROM");
		return query(rs->ResultMapper.asObject(rs, Integer.class), needDebug(), sql, getParams());
	}
	/**
	 * 统计记录数
	 * 
	 * @return
	 * @author yingjianhua
	 */
	protected Integer countTotal() {
		String sql = getSql();
		int s = sql.indexOf(" LIMIT");
		if(s!=-1)
			sql = sql.substring(0, s);
		sql = sql.replaceFirst("(select|SELECT)\\s+.*\\s+(FROM|from)", "SELECT COUNT(1) FROM");
		return query(rs->ResultMapper.asObject(rs, Integer.class), needDebug(), sql, getParams());
	}
	
	/**
	 * 根据字段名将数据注入bean
	 * @author yingjianhua
	 */
	protected <T extends Bean<?, ?>> List<T> queryBeans(Class<T> beanClass) {
		return query(rs->ResultMapper.asBeanList(rs, beanClass));
	}
	
	/**
	 * 根据字段名将数据注入bean
	 * @author yingjianhua
	 */
	protected <T extends Bean<?, ?>> T queryBean(Class<T> beanClass) {
		return query(rs->ResultMapper.asBean(rs, beanClass));
	}
	/**
	 * 根据字段名将数据注入entity
	 * @author yingjianhua
	 */
	protected <T extends Entity> T queryEntity(Class<T> entityClass) {
		return query(rs->ResultMapper.asEntity(rs, entityClass));
	}
	
	/**
	 * 根据字段名将数据注入entity
	 * @author yingjianhua
	 */
	protected <T extends Entity> List<T> queryEntitys(Class<T> entityClass) {
		return query(rs->ResultMapper.asEntityList(rs, entityClass));
	}
	/**
	 * 执行sql语句
	 * @author yingjianhua
	 */
	protected int executeUpdate() {
		printSql(getSql(), getParams());
		PreparedStatement stmt = null;
		try {
			stmt = ConnectionManager.getConnection().prepareStatement(getSql());
			BeanBase.toPreparedStatementData(stmt, 1, getParams());
			return stmt.executeUpdate();
		} catch (Exception e) {
			logger.error("执行executeUpdate出错:{}", e.getMessage());
			throw LOG.err("executeUpdate", "执行【{0}】出错", getSql());
		} finally {
			close(stmt);
		}
	}
	/**
	 * 执行sql语句
	 * @author yingjianhua
	 */
	protected Serializable executeUpdateReturnGeneratedKey(ColumnTypes type) {
		printSql(getSql(), getParams());
		PreparedStatement stmt = null;
		try {
			stmt = ConnectionManager.getConnection().prepareStatement(getSql(), Statement.RETURN_GENERATED_KEYS);
			BeanBase.toPreparedStatementData(stmt, 1, getParams());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			return (Serializable)ResultMapper.asColumn(rs, type, "GENERATED_KEY");
		} catch (Exception e) {
			logger.error("执行executeUpdate出错:{}", e.getMessage());
			throw LOG.err("executeUpdate", "执行【{0}】出错", getSql());
		} finally {
			close(stmt);
		}
	}

	protected static <R> R query(Function<ResultSet, R> f, boolean debug, String sql, Serializable... params) {
		printSql(sql, params);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionManager.getConnection().prepareStatement(sql);
			stmt.setFetchSize(AppConfig.query_fetchsize);
			stmt.setFetchSize(BeanBase.FETCH_SIZE);
			BeanBase.toPreparedStatementData(stmt, 1, params);
			rs = stmt.executeQuery();
			return f.apply(rs);
		} catch (Exception e) {
			throw LOG.err(e, "queryCountRecord", "取数据库记录时出错【{0}】!", sql);
		} finally {
			close(stmt, rs);
		}
	}
	
	protected <R> R query(Function<ResultSet, R> f) {
		printSql(getSql(), getParams());
//		log.debug("sql:"+getSql()+"|"+params(getParams()));
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionManager.getConnection().prepareStatement(getSql());
			stmt.setFetchSize(AppConfig.query_fetchsize);
			stmt.setFetchSize(BeanBase.FETCH_SIZE);
			BeanBase.toPreparedStatementData(stmt, 1, getParams());
			rs = stmt.executeQuery();
			return f.apply(rs);
		} catch (Exception e) {
			throw LOG.err(e, "queryRecord", "取数据库记录时出错【{0}】!", getSql());
		} finally {
			close(stmt, rs);
		}
	}

	private static final void close(Statement stmt, ResultSet rs) {
		close(rs);
		close(stmt);
	}
	private static final void close(Statement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception e) {
			throw LOG.err(e, "closeStmt", "关闭对象【Statement】出错");
		}
	}
	private static final void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			throw LOG.err(e, "closeResultSet", "关闭对象【ResultSet】出错");
		}
	}
	private static void printSql(String sql, Serializable... params) {
		Optional<StackTraceElement> o = Stream.of(new Throwable().getStackTrace()).limit(10).filter(st->st.getClassName().endsWith("Dao")||st.getClassName().contains("Dao$")).findFirst();
		if(o.isPresent()) {
			logger.debug("sql:"+sql+"|"+params(params)+"] [stackTrace: "+o.get().toString());
		} else {
			Optional<StackTraceElement> o2 = Stream.of(new Throwable().getStackTrace()).limit(10).filter(st->st.getClassName().endsWith("Action")||st.getClassName().contains("Action$")).findFirst();
			if(o2.isPresent())
				logger.debug("sql:"+sql+"|"+params(params)+"] [stackTrace: "+o.get().toString());
			else
				logger.debug("sql:"+sql+"|"+params(params));
		}
	}
	
	private static String params(Serializable... a) {
		if (a == null)
            return "params:null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "params:[]";

        StringBuilder b = new StringBuilder();
        b.append("params:");
        for (int i = 0; ; i++) {
        	if(a[i] instanceof String)
        		b.append("\"").append(a[i]).append("\"");
        	else
        		b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.toString();
            b.append(", ");
        }
	}
}
