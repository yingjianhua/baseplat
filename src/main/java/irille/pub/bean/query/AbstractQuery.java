package irille.pub.bean.query;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.svr.DbPool;

public abstract class AbstractQuery {
	private static final Log LOG = new Log(AbstractQuery.class);
	private static final Logger log = Logger.getLogger(AbstractQuery.class);
	
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
	 * 执行sql语句
	 * @author yingjianhua
	 */
	protected int executeUpdate() {
		printSql(getSql(), getParams());
//		if(needDebug()) printSql(getSql(), getParams());
		PreparedStatement stmt = null;
		try {
			stmt = DbPool.getInstance().getConn().prepareStatement(getSql());
			BeanBase.toPreparedStatementData(stmt, 1, getParams());
			return stmt.executeUpdate();
		} catch (Exception e) {
			throw LOG.err("executeUpdate", "执行【{0}】出错", getSql());
		} finally {
			DbPool.close(stmt);
		}
	}

	protected static <R> R query(Function<ResultSet, R> f, boolean debug, String sql, Serializable... params) {
		printSql(sql, params);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbPool.getInstance().getConn().prepareStatement(sql);
			stmt.setFetchSize(BeanBase.FETCH_SIZE);
			BeanBase.toPreparedStatementData(stmt, 1, params);
			rs = stmt.executeQuery();
			return f.apply(rs);
		} catch (Exception e) {
			throw LOG.err(e, "queryCountRecord", "取数据库记录时出错【{0}】!", sql);
		} finally {
			DbPool.close(stmt, rs);
		}
	}
	
	protected <R> R query(Function<ResultSet, R> f) {
		printSql(getSql(), getParams());
//		log.debug("sql:"+getSql()+"|"+params(getParams()));
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbPool.getInstance().getConn().prepareStatement(getSql());
			stmt.setFetchSize(BeanBase.FETCH_SIZE);
			BeanBase.toPreparedStatementData(stmt, 1, getParams());
			rs = stmt.executeQuery();
			return f.apply(rs);
		} catch (Exception e) {
			throw LOG.err(e, "queryRecord", "取数据库记录时出错【{0}】!", getSql());
		} finally {
			DbPool.close(stmt, rs);
		}
	}
	
	private static void printSql(String sql, Serializable... params) {
		Optional<StackTraceElement> o = Stream.of(new Throwable().getStackTrace()).limit(10).filter(st->st.getClassName().endsWith("Dao")||st.getClassName().contains("Dao$")).findFirst();
		if(o.isPresent()) {
			log.debug("sql:"+sql+"|"+params(params)+"] [stackTrace: "+o.get().toString());
		} else {
			Optional<StackTraceElement> o2 = Stream.of(new Throwable().getStackTrace()).limit(10).filter(st->st.getClassName().endsWith("Action")||st.getClassName().contains("Action$")).findFirst();
			if(o2.isPresent())
				log.warn("数据库查询没有写在Dao里面,有问题... "+o2.get().toString());
			else
				log.warn("数据库查询没有写在Dao里面,有问题... 未知位置!!!");
			log.debug("sql:"+sql+"|"+params(params));
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
