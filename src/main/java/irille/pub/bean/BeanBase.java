package irille.pub.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import irille.core.sys.SysTable;
import irille.pub.ClassTools;
import irille.pub.Cn;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.idu.Idu;
import irille.pub.svr.DbPool;
import irille.pub.svr.Env;
import irille.pub.svr.Env.SysConf;
import irille.pub.svr.ISvrVars;
import irille.pub.tb.Fld;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;
import irille.pub.tb.TbBase;
import irille.pub.valid.ValidDate;
import irille.pub.valid.ValidNumber;
import irille.pub.view.Fmts;
import junit.framework.TestResult;

/**
 * Title: 数据模型基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public abstract class BeanBase<BEAN extends BeanBase, KEY extends Serializable> implements  ISvrVars, Serializable {
	private static final Log LOG = new Log(BeanBase.class);
	public static final int FETCH_SIZE = Env.getConfParaInt(SysConf.FETCH_SIZE);
	//	public static final Sys.T SYS = Sys.T.USER;
	public static final String CODE_SEPARATOR = Env.getConfParaStr(SysConf.CODE_SEPARATOR); //代码的分隔符
	public static final Cn CN_LINES = new Cn("lines", "明细");
	private static final char[] SUFFIX = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

	public abstract KEY getPkey();

	@JsonIgnore
	public Short getRowVersion() {
		return -1;
	}

	public void setRowVersion(Short rv) {
	}

	/**
	 * 产生Bean的空实例
	 * 
	 * @param beanClass
	 *          Bean的类
	 * @return
	 */
	public static <T extends Bean> T newInstance(Class<T> beanClass) {
		return (T) ClassTools.newInstance(beanClass);
	}

	public final static Tb tb(Class beanClazz) {
		return Tb.getTBByBean(beanClazz);
	}

	/**
	 * 新建Bean实例
	 * 
	 * @param id
	 * @return 结果
	 */
	public static Bean newBean(int id) {
		return (Bean) ClassTools.newInstance(SysTable.gtTable(id).gtClazz());
	}

	/**
	 * 根据Bean的Id取Bean的类
	 */
	public final static Class<Bean> clazz(int id) {
		return SysTable.gtTable(id).gtClazz();
	}

	/**
	 * 根据表Id号与主键取记录
	 * 
	 * @param id
	 * @param pkey
	 * @return
	 */
	public static Bean get(int id, Serializable... pkey) {
		return get(SysTable.gtTable(id).gtClazz(), pkey);
	}

	/**
	 * 根据主键值删除记录
	 * 
	 * @param pkeys
	 */
	public void del(Serializable... pkeys) {
		del(Bean.get(clazz(), pkeys));
	}

	/**
	 * 取Bean的类
	 * 
	 * @return
	 */
	public abstract Class clazz();

	public abstract TbBase gtTB();

	/**
	 * 根据唯一索引取单一记录
	 * 
	 * @param idx
	 * @param lockFlag
	 * @param values
	 * @return
	 */
	public static final Bean loadUnique(Index idx, boolean lockFlag, Serializable... values) {
		Bean bean = chkUnique(idx, lockFlag, values);
		if (bean != null)
			return bean;
		throw LOG.err("recordNotFound", "表【{0}】条件为【{1}】的记录不存在！", idx.getTb().getName(), idx.toString(values));
	}

	public static final Bean chkUnique(Index idx, boolean lockFlag, Serializable... values) {
		return chkUnique(idx.getTb().getClazz(), idx.getSelectSql(), lockFlag, values);
	}

	public static final Bean chkUnique(Class clazz, String sql, boolean lockFlag, Serializable... values) {
		Bean bean;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			sql = sql + Bean.lockSqlStr(lockFlag);
			stmt = DbPool.getInstance().getConn().prepareStatement(sql);
			stmt.setFetchSize(2);
			toPreparedStatementData(stmt, 1, values);
			rs = stmt.executeQuery();
			if (!rs.next())
				return null;
			bean = newInstance(clazz);
			bean.fromResultSet(rs);
			if (rs.next())
				throw LOG.err("chkUnique", "取唯一记录出错，记录非唯一!");
		} catch (Exception e) {
			e.printStackTrace();
			throw LOG.err("query", "查询数据出错，SQL=【{0}】", sql);
		} finally {
			DbPool.close(stmt, rs);
		}
		return bean;
	}

	public static Map[] executeQueryMap(String sql, Serializable... paras) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbPool.getInstance().getConn()
			    .prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setFetchSize(FETCH_SIZE);
			toPreparedStatementData(stmt, 1, paras);
			rs = stmt.executeQuery();
			rs.last();
			Map[] maps = new Map[rs.getRow()];
			rs.first();
			for (int i = 0; i < maps.length; i++, rs.next()) {
				maps[i] = new HashMap<String, Object>();
				for (int j = 0; j < rs.getMetaData().getColumnCount(); j++) {
					maps[i].put(rs.getMetaData().getColumnName(j + 1), rs.getObject(j + 1));
				}
			}
			return maps;
		} catch (SQLException e) {
			throw LOG.err(e, "executeQueryOneRow", "取数据库记录时出错!");
		} finally {
			DbPool.close(rs);
			DbPool.close(stmt);
		}
	}

	// TODO
	// 增加资源权限的条件
	// public static Map[] executeQueryMap(String sql, Tb tb, Serializable...
	// paras) {
	// sql = ResCtrl.getInstance().getSqlRes(sql, tb);
	// return executeQueryMap(sql, paras);
	// }

	public static int executeUpdate(String sql, Serializable... paras) {
		PreparedStatement stmt = null;
		try {
			stmt = DbPool.getInstance().getConn().prepareStatement(sql);
			toPreparedStatementData(stmt, 1, paras);
			return stmt.executeUpdate();
		} catch (Exception e) {
			throw LOG.err("executeUpdate", "执行【{0}】出错", sql);
		} finally {
			DbPool.close(stmt);
		}
	}

	/**
	 * SELECT数据到数组中
	 * 
	 * @param sql
	 *          SQL语句
	 * @return
	 */
	public static List<Object[]> list(String sql, Serializable... paras) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbPool.getInstance().getConn().prepareStatement(sql);
			stmt.setFetchSize(FETCH_SIZE);
			toPreparedStatementData(stmt, 1, paras);
			rs = stmt.executeQuery();
			Vector<Object[]> list = new Vector<Object[]>();
			while (rs.next()) {
				list.add(resultSetToObjs(rs));
			}
			return list;
		} catch (Exception e) {
			throw LOG.err(e, "queryVector", "取数据库记录时出错【{0}】!", sql);
		} finally {
			DbPool.close(rs);
			DbPool.close(stmt);
		}
	}
	
	/**
	 * 统计记录数
	 * 
	 * @return
	 * @author yingjianhua
	 */
	public static <T extends Bean> Integer countRecord(Class<T> beanClass, String whereSql, Serializable... paras) {
		if (whereSql == null || whereSql.trim().isEmpty())
			whereSql = "1=1";
		String sql = "SELECT COUNT(*) FROM " + Tb.getTBByBean(beanClass).getCodeSqlTb() + " WHERE " + whereSql ;
		return countRecord(sql, paras);
	}
	/**
	 * 统计记录数
	 * 
	 * @return
	 * @author yingjianhua
	 */
	public static Integer countRecord(String sql, Serializable... paras) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbPool.getInstance().getConn().prepareStatement(sql);
			stmt.setFetchSize(FETCH_SIZE);
			toPreparedStatementData(stmt, 1, paras);
			rs = stmt.executeQuery();
			if (!rs.next())
				throw LOG.err("oneRowNotFound", "单一记录没找到【{0}】!", sql);
			return rs.getInt(1);
		} catch (Exception e) {
			throw LOG.err(e, "executeCountRecord", "取数据库记录时出错【{0}】!", sql);
		} finally {
			DbPool.close(stmt, rs);
		}
	}

	/**
	 * 取一条记录，如有多条则取第一条
	 * 
	 * @param sql
	 *          SQL语句
	 * @return
	 */
	public static Object[] queryOneRow(String sql, Serializable... paras) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbPool.getInstance().getConn().prepareStatement(sql);
			stmt.setFetchSize(FETCH_SIZE);
			toPreparedStatementData(stmt, 1, paras);
			rs = stmt.executeQuery();
			if (!rs.next())
				throw LOG.err("oneRowNotFound", "单一记录没找到【{0}】!", sql);
			return resultSetToObjs(rs);
		} catch (Exception e) {
			throw LOG.err(e, "executeQueryOneRow", "取数据库记录时出错【{0}】!", sql);
		} finally {
			DbPool.close(stmt, rs);
		}
	}

	public static Object[] queryOneRowIsNull(String sql, Serializable... paras) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbPool.getInstance().getConn().prepareStatement(sql);
			stmt.setFetchSize(FETCH_SIZE);
			toPreparedStatementData(stmt, 1, paras);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				return null;
			}
			return resultSetToObjs(rs);
		} catch (Exception e) {
			throw LOG.err(e, "executeQueryOneRow", "取数据库记录时出错【{0}】!", sql);
		} finally {
			DbPool.close(stmt, rs);
		}
	}

	public static Object[] resultSetToObjs(ResultSet rs) {
		try {
			Object[] d = new Object[rs.getMetaData().getColumnCount()];
			for (int i = 1; i <= d.length; i++) {
				d[i - 1] = rs.getObject(i);
			}
			return d;
		} catch (Exception e) {
			throw LOG.err(e, "executeQueryOneRow", "取数据库记录时出错!");
		}
	}

	/**
	 * 取主表主键为“序号+6位表ID”的明细行
	 * @param <T>
	 * @param lineClass 明细表类型
	 * @param main 主表Bean
	 * @param lockFlag 是否锁定记录
	 * @return
	 */
	public static <T extends Bean> List<T> listTid(Class<T> lineClass, BeanLong main, boolean lockFlag) {
		long mainpkey = Idu.lineFirstPkey(main.getPkey());
		long mainpkey2 = mainpkey + 100000;
		int num = 0;
		String wheresql = "pkey > " + mainpkey + " and pkey < " + mainpkey2;
		return BeanBase.list(lineClass, wheresql, lockFlag); // 数据库旧数据
	}

	/**
	 * 查询数据库记录到Vector对象中
	 * 
	 * @param beanClass
	 *          Bean类
	 * @param whereSql
	 *          WHERE之后的语句内容，如:code=? AND name=?,问号的个数与后面参数个数一致
	 * @param vals
	 *          Sql中的变量
	 * @return list对象
	 */
	public static <T extends Bean> List<T> list(Class<T> beanClass, String whereSql, boolean lockFlag,
	    Serializable... vals) {
		return list(0, beanClass, lockFlag, whereSql, vals);
	}

	/**
	 * 查询数据库记录到Vector对象中
	 * 
	 * @param maxRows
	 *          最的最大行数，0表示全部
	 * @param beanClass
	 *          Bean类
	 * @param whereSql
	 *          WHERE之后的语句内容，如:code=? AND name=?,问号的个数与后面参数个数一致
	 * @param vals
	 *          Sql中的变量
	 * @return list对象
	 */
	public static <T extends Bean> List<T> list(int maxRows, Class<T> beanClass, boolean lockFlag, String whereSql,
	    Serializable... vals) {
		T bean;
		Vector<T> list = new Vector<T>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (whereSql == null || whereSql.trim().isEmpty())
			whereSql = "1=1";
		try {
			stmt = DbPool
			    .getInstance()
			    .getConn()
			    .prepareStatement(
			        "SELECT * FROM " + Tb.getTBByBean(beanClass).getCodeSqlTb() + " WHERE " + whereSql + lockSqlStr(lockFlag));
			stmt.setFetchSize(maxRows);
			toPreparedStatementData(stmt, 1, vals);
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean = newInstance(beanClass);
				bean.fromResultSet(rs);
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw LOG.err("query", "查询数据出错!");
		} finally {
			DbPool.close(stmt, rs);
		}
		return list;
	}
	
	/**
	 * 联合查询数据库记录到Vector对象中
	 * @author wanghaixiao
	 * @param maxRows		最的最大行数，0表示全部
	 * @param beanClass		用于注入的bean类型
	 * @param beanClasses	需要关联查询的类
	 * @param unionFlds		关联条件的字段
	 * @param whereFlds		条件中的字段 数组中字段可以大于条件中的{数字}，但顺序必须对应，例如数组下标为4的字段必须对应{4}
	 * @param lockFlag		锁定标识
	 * @param whereSql		条件语句, "{0}=? and {1}>?"
	 * @param vals			条件参数 入参数量可以大于条件中的？，但下标必须对应,例如第4个参数必须对应第{3}个字段的?
	 * @return				返回注入后的对象列表
	 */
	public static <T extends Bean> List<T> list(int maxRows, Class<T> beanClass, Class<T>[] beanClasses, IEnumFld[] unionFlds, IEnumFld[] whereFlds, boolean lockFlag, String whereSql, Serializable... vals) {
		return list(maxRows, beanClass, beanClasses, null, unionFlds, whereFlds, lockFlag, whereSql, 0, 0, vals);
	}
	
	/**
	 * 联合查询数据库记录到Vector对象中
	 * @author wanghaixiao
	 * @param beanClass		用于注入的bean类型
	 * @param beanClasses	需要关联查询的类
	 * @param unionFlds		关联条件的字段
	 * @param whereFlds		条件中的字段 数组中字段可以大于条件中的{数字}，但顺序必须对应，例如数组下标为4的字段必须对应{4}
	 * @param lockFlag		锁定标识
	 * @param idx			分页起始索引
	 * @param count			查询条数
	 * @param whereSql		条件语句, "{0}=? and {1}>?"
	 * @param vals			条件参数 入参数量可以大于条件中的？，但下标必须对应,例如第4个参数必须对应第{3}个字段的?
	 * @return				返回注入后的对象列表
	 */
	public static <T extends Bean> List<T> list(Class<T> beanClass, Class<T>[] beanClasses, IEnumFld[] unionFlds, IEnumFld[] whereFlds, boolean lockFlag, String whereSql, int idx, int count, Serializable... vals) {
		return list(0, beanClass, beanClasses, null, unionFlds, whereFlds, lockFlag, idx, count, whereSql, vals);
	}
	
	/**
	 * 联合查询数据库记录到Vector对象中
	 * @author wanghaixiao
	 * @param maxRows		最的最大行数，0表示全部
	 * @param beanClass		用于注入的bean类型
	 * @param beanClasses	需要关联查询的类
	 * @param selectFlds	返回结果集需要的字段
	 * @param unionFlds		关联条件的字段
	 * @param whereFlds		条件中的字段 数组中字段可以大于条件中的{数字}，但顺序必须对应，例如数组下标为4的字段必须对应{4}
	 * @param lockFlag		锁定标识
	 * @param whereSql		条件语句, "{0}=? and {1}>?"
	 * @param vals			条件参数 入参数量可以大于条件中的？，但下标必须对应,例如第4个参数必须对应第{3}个字段的?
	 * @return				返回注入后的对象列表
	 */
	public static <T extends Bean> List<T> list(int maxRows, Class<T> beanClass, Class<T>[] beanClasses, IEnumFld[] selectFlds, IEnumFld[] unionFlds, IEnumFld[] whereFlds, boolean lockFlag, String whereSql, Serializable... vals) {
		return list(maxRows, beanClass, beanClasses, selectFlds, unionFlds, whereFlds, lockFlag, 0, 0, whereSql, vals);
	}
	
	/**
	 * 联合查询数据库记录到Vector对象中
	 * @author wanghaixiao
	 * @param beanClass		用于注入的bean类型
	 * @param beanClasses	需要关联查询的类
	 * @param selectFlds	返回结果集需要的字段
	 * @param unionFlds		关联条件的字段
	 * @param whereFlds		条件中的字段 数组中字段可以大于条件中的{数字}，但顺序必须对应，例如数组下标为4的字段必须对应{4}
	 * @param lockFlag		锁定标识
	 * @param idx			分页起始索引
	 * @param count			查询条数
	 * @param whereSql		条件语句, "{0}=? and {1}>?"
	 * @param vals			条件参数 入参数量可以大于条件中的？，但下标必须对应,例如第4个参数必须对应第{3}个字段的?
	 * @return				返回注入后的对象列表
	 */
	public static <T extends Bean> List<T> list(Class<T> beanClass, Class<T>[] beanClasses, IEnumFld[] selectFlds, IEnumFld[] unionFlds, IEnumFld[] whereFlds, boolean lockFlag, String whereSql, int idx, int count, Serializable... vals) {
		return list(0, beanClass, beanClasses, selectFlds, unionFlds, whereFlds, lockFlag, idx, count, whereSql, vals);
	}
	
	/**
	 * 联合查询数据库记录到Vector对象中
	 * @author wanghaixiao
	 * @param maxRows		最的最大行数，0表示全部
	 * @param beanClass		用于注入的bean类型
	 * @param beanClasses	需要关联查询的类
	 * @param selectFlds	返回结果集需要的字段
	 * @param unionFlds		关联条件的字段
	 * @param whereFlds		条件中的字段 数组中字段可以大于条件中的{数字}，但顺序必须对应，例如数组下标为4的字段必须对应{4}
	 * @param lockFlag		锁定标识
	 * @param idx			分页起始索引
	 * @param count			查询条数
	 * @param whereSql		条件语句, "{0}=? and {1}>?"
	 * @param vals			条件参数 入参数量可以大于条件中的？，但下标必须对应,例如第4个参数必须对应第{3}个字段的?
	 * @return				返回注入后的对象列表
	 */
	public static <T extends Bean> List<T> list(int maxRows, Class<T> beanClass, Class<T>[] beanClasses, IEnumFld[] selectFlds, IEnumFld[] unionFlds, IEnumFld[] whereFlds, boolean lockFlag, int idx, int count, String whereSql, Serializable... vals) {
		if (unionFlds.length == 0)
			return null;
		String selectSql = "";
		String tbSql = "";
		String pwSql = "";
		Fld[] selFlds = null;
		Map<Class<T>, String> map = initSuffix(beanClasses);
		T bean;
		Vector<T> list = new Vector<T>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (selectFlds != null && selectFlds.length > 0) {
			selFlds = toFlds(selectFlds);
			chkFldsInClass(beanClass, selFlds);
			for (Fld fld : selFlds)
				selectSql += map.get(beanClass) + "." + fld.getCodeSqlField() + ",";
			selectSql = selectSql.substring(0, selectSql.length() - 1);
		} else
			selectSql = "*";
		for (Class<T> c : beanClasses) 
			tbSql += Tb.getTBByBean(c).getCodeSqlTb() + " " + map.get(c) + ",";
		for (Class<T> c : beanClasses) {
			Fld[] flds = Tb.getTBByBean(c).getFlds();
			for (Fld f : flds) {
				for (Class<T> sc : beanClasses)
					if (f.isOutKey() && ((FldOutKey) f).getOutTbClazz() == sc)
						if (unionFlds == null && f.getCodeSqlField().equals(Tb.getTBByBean(sc).getCodeNoPackage()) || unionFlds != null && isInArray(f, unionFlds))
							 pwSql += map.get(c) + "." + f.getCodeSqlField() + "=" + map.get(sc) + ".pkey AND ";
			}
		}
		tbSql = tbSql.substring(0, tbSql.length() - 1);
		if (Str.isEmpty(whereSql))
			whereSql = pwSql.substring(0, pwSql.length() - 4);
		else {
			vals = reBuildValsByWhereSql(whereSql, vals);
			String[] parasName = new String[whereFlds.length];
			for (int i=0; i<whereFlds.length; i++)
				parasName[i] = map.get(whereFlds[i].getFld().getTb().getClazz()) + "." + whereFlds[i].getFld().getCodeSqlField();
			whereSql = pwSql + Log.messageToString(whereSql, parasName);
		}
		
		String sql = "select " + selectSql + " from " + tbSql + " where " + whereSql;
		System.err.println(sql);
		
		try {
			stmt = DbPool
			    .getInstance()
			    .getConn()
			    .prepareStatement(
			    		getPageSql(sql, lockFlag, idx, count));
			stmt.setFetchSize(maxRows);
			toPreparedStatementData(stmt, 1, vals);
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean = newInstance(beanClass);
				if (selectFlds != null && selectFlds.length > 0)
					bean.fromResultSet(rs, selFlds);
				else
					bean.fromResultSet(rs);
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw LOG.err("query", "查询数据出错!");
		} finally {
			DbPool.close(stmt, rs);
		}
		return list;
	}
	public static void main(String[] args) {
//		SysUser.TB.getCode();
//		SysEm.TB.getCode();
//		SysDept.TB.getCode();
//		
//		String whereSql = "{0}=?";
//		Class[] beanClasses = {SysUser.class, SysDept.class, SysEm.class};
//		IEnumFld[] unionFlds = {SysUser.T.DEPT,SysEm.T.DEPT};
//		IEnumFld[] selectFlds = {SysUser.T.NICKNAME,SysUser.T.NAME, SysUser.T.PKEY,SysUser.T.DEPT};
//		IEnumFld[] whereFlds = {SysDept.T.PKEY, SysEm.T.NAME};
//		List<SysUser> list = SysUser.list(0, SysUser.class, beanClasses, selectFlds, unionFlds, whereFlds, false, whereSql, 1);
//		for (SysUser user : list)
//			System.out.println("name:"+user.getName()+" | nick:"+user.getNickname()+" | dept:"+user.getDept() + "| pkey:" + user.getPkey());
		String string = "{0},12,,{1}, {3}";
		
		for (Serializable str : reBuildValsByWhereSql(string , "zero","one"))
			System.out.println(str);
	
	}
	
	/**
	 * 根据whereSql重建vals，对应{数字}的下标截取vals
	 * @param whereSql
	 * @param vals
	 * @return
	 */
	private static Serializable[] reBuildValsByWhereSql(String whereSql, Serializable...vals) {
		
		List<Integer> suffix = matchWhereSql(whereSql);
		List<Serializable> result = new ArrayList<>();
		for (Integer s : suffix)
			if (s.intValue() < vals.length)
				result.add(vals[s.intValue()]);
		return result.toArray(new Serializable[result.size()]);
	}
	
	/**
	 * 匹配str中的{数字}格式数字并根据顺序和数量返回对应的数组
	 * @author wanghaixiao
	 * @param str
	 * @return
	 */
	private static List<Integer> matchWhereSql(String str) {
        Pattern pattern = Pattern.compile("\\{(\\d+)\\}");
        Matcher matcher = pattern.matcher(str);
        List<Integer> list = new ArrayList<>();
        while (matcher.find())
        	list.add(Integer.valueOf(matcher.group(1)));
        return list;
	}
	
	
	/**
	 * 为BeanClasses中的类初始化下标，下标为a到z
	 * @author wanghaixiao
	 * @param beanClasses
	 * @return
	 */
	private static <T> Map<Class<T>, String> initSuffix(Class<T>[] beanClasses) {
		Map<Class<T>, String> map = new HashMap<>();
		for (int i=0; i<beanClasses.length; i++) {
			map.put(beanClasses[i], String.valueOf(SUFFIX[i]));
		}
		return map;
	}
	
	/**
	 * 判断Fld是否包含在flds数组中
	 * @author wanghaixiao
	 * @param fld
	 * @param flds
	 * @return
	 */
	private static boolean isInArray(Fld fld, IEnumFld[] flds) {
		for (IEnumFld line : flds)
			if (fld == line.getFld())
				return true;
		return false;
	}
	
	/**
	 * 校验Fld数组中字段是否都属于beanClass类中的字段
	 * @author wanghaixiao
	 * @param beanClass
	 * @param flds
	 */
	private static void chkFldsInClass(Class beanClass, Fld[] flds) {
		for (Fld fld : flds)
			if (fld.getTb().getClazz() != beanClass)
				throw LOG.err("selectFlds", "注入类型【{0}】不包含查询字段【{1}】", beanClass.getName(), fld.getCode());
	}
	
	/**
	 * IEnumFld数组转为Fld数组
	 * @author wanghaixiao
	 * @param enums
	 * @return
	 */
	private static Fld[] toFlds(IEnumFld[] enums) {
		Fld[] flds = new Fld[enums.length];
		for (int i=0; i<enums.length; i++)
			flds[i] = enums[i].getFld();
		return flds;
	}
	
	

	/**
	 * 分页方法
	 * 
	 * @param <T>
	 * @param beanClass
	 *          BEAN类
	 * @param lockFlag
	 *          锁定标识
	 * @param whereSql
	 *          条件语句, "name=? and code=?"
	 * @param idx
	 *          分页的起始位置
	 * @param fetchCount
	 *          一页取多少记录
	 * @param vals
	 *          条件参数
	 * @return
	 */
	public static <T extends Bean> List<T> list(Class<T> beanClass, boolean lockFlag, String whereSql, int idx,
	    int fetchCount, Serializable... vals) {
		T bean;
		Vector<T> list = new Vector<T>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (whereSql == null || whereSql.trim().isEmpty())
			whereSql = "1=1";
		try {
			String sql = getPageSql(Tb.getTBByBean(beanClass).getCodeSqlTb(), lockFlag, whereSql, idx, fetchCount);
			//System.out.println("--------------------------==----" + sql);
			stmt = DbPool.getInstance().getConn().prepareStatement(sql);
			toPreparedStatementData(stmt, 1, vals);
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean = newInstance(beanClass);
				bean.fromResultSet(rs);
				list.add(bean);
			}
		} catch (Exception e) {
			System.err.println(e.toString());
			throw LOG.err("query", "查询数据出错!");
		} finally {
			DbPool.close(stmt, rs);
		}
		return list;
	}

	private static String getPageSql(String table, boolean lockFlag, String where, int idx, int count) {
		String sql = "SELECT * FROM " + table + " WHERE " + where;
		return getPageSql(sql, lockFlag, idx, count);
	}

	public static String getPageSql(String sql, boolean lockFlag, int idx, int count) {
		return Env.INST.getDB().getPageSql(sql, lockFlag, idx, count);
	}

	/**
	 * 转化为金额大写的表示
	 * 
	 * @param amt
	 *          金额
	 * @return 结果
	 */
	public static String currency(BigDecimal amt) {
		return Fmts.FMT_DEC2_CH_UPPER.out(null, amt);
	}

	/**
	 * 
	 * @param negativeZeroPlus
	 * @param fields
	 * @see irille.svr.pub.ModelBaseInf#assertDecimal(int, irille.pub.tb.Fld)
	 */
	public void assertDecimal(int negativeZeroPlus, Fld... fields) {
		BigDecimal val;
		for (Fld field : fields) {
			val = (BigDecimal) ClassTools.getProperty(this, field.getCode());
			if (!ValidNumber.check(negativeZeroPlus, val))
				throw LOG.err("valudNotVaild", "{0}的{1}的值[{2}]无效!", this, field.getName(), currency(val));
		}
	}

	public void assertDecimalCanNull(int negativeZeroPlus, Fld... fields) {
		BigDecimal val;
		for (Fld field : fields) {
			val = (BigDecimal) ClassTools.getProperty(this, field.getCode());
			if (val == null)
				continue;
			if (!ValidNumber.check(negativeZeroPlus, val))
				throw LOG.err("valudNotVaild", "{0}的{1}的值[{2}]无效!", this, field.getName(), currency(val));
		}
	}

	/**
	 * 取对象，如何BeanBuf中已有，则不从数据库中取，否则从数据库中取回并根据规则存到BeanBuf中
	 * 
	 * @param beanClass
	 * @param pkeys
	 * @return
	 */
	public static <T extends Bean> T get(Class<T> beanClass, Serializable... pkeys) {
		return (T) BeanBuf.get(beanClass, pkeys);
	}

	/**
	 * 从数据库中取对象
	 * 
	 * @param beanClass
	 * @param pkeys
	 * @return
	 */
	public static <T extends Bean> T load(Class<T> beanClass, Serializable... pkeys) {
		return (T) newInstance(beanClass).load(pkeys);
	}

	// /**
	// * 从数据库中取对象
	// *
	// * @param beanCode
	// * @param pkeys
	// * @return
	// */
	// public static Bean load(String beanCode, Serializable... pkeys) {
	// return newInstance(Tb.getTBByCode(beanCode).getClazz()).load(pkeys);
	// }

	/**
	 * 取数据并加锁，以便修改
	 * 
	 * @param beanClass
	 * @param pkeys
	 * @return
	 */
	public static final <T extends Bean> T loadAndLock(Class<T> beanClass, Serializable... pkeys) {
		return (T) newInstance(beanClass).loadAndLock(pkeys);
	}

	/**
	 * 检测数据,没找到返回null
	 * 
	 * @param beanClass
	 * @param pkeys
	 * @return
	 */
	public static final <T extends Bean> T chk(Class<T> beanClass, Serializable... pkeys) {
		return (T) newInstance(beanClass).chk(pkeys);
	}

	public static final <T extends Bean> T getByLinePkey(Class<T> beanClass, Long linekey) {
		Long pkey = linekey / SysTable.NUM_BASE;
		return (T) newInstance(beanClass).chk(pkey);
	}

	/**
	 * 检测数据并加锁，以便修改，没找到返回null
	 * 
	 * @param beanClass
	 * @param pkeys
	 * @return
	 */
	public static final <T extends Bean> T chkAndLock(Class<T> beanClass, Serializable... pkeys) {
		return (T) newInstance(beanClass).chkAndLock(pkeys);
	}

	public static final int statementSetData(PreparedStatement stmt, int firstId, Serializable... values) {
		try {
			for (Serializable v : values) {
				stmt.setObject(firstId++, v);
			}
		} catch (Exception e) {
			throw LOG.err(e, "statementSet", "赋值给对象【PreparedStatement】时出错!");
		}
		return firstId;
	}

	/**
	 * 将对象的值置到PreparedStatement对象中
	 * 
	 * @param stmt
	 * @param firstFldId
	 * @param vals
	 *          参数值列表
	 * @return
	 */
	public final static int toPreparedStatementData(PreparedStatement stmt, int firstFldId, Serializable... vals) {
		try {
			int j = firstFldId;
			for (int i = 0; i < vals.length; i++) {
				stmt.setObject(j, vals[i]);
				j++;
			}
			return j;
		} catch (Exception e) {
			throw LOG.err(e, "toPreparedStatement", "对对象【PreparedStatement】赋值时出错!");
		}
	}

	/**
	 * 取记录锁的SQL语句
	 * 
	 * @param lockFlag
	 * @return
	 */
	public static String lockSqlStr(boolean lockFlag) {
		return lockFlag ? Env.INST.getDB().lock() : "";
	}

	/**
	 * 取当前时间
	 * 
	 * @return
	 */
	public static Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static Date nowTime() {
		return new Date();
	}

	/**
	 * 去掉日期的时分秒
	 * 
	 * @param date
	 *          日期
	 */
	public static final Date date000000(Date date) {
		return ValidDate.trimTo000000(date);
	}

	/**
	 * 去掉日期的时分秒
	 * 
	 * @param date
	 *          日期
	 */
	public static final Date date235959(Date date) {
		return ValidDate.trimTo235959(date);
	}

	/**
	 * 金额四舍五入
	 * 
	 * @param field
	 *          字段
	 * @param amt
	 *          金额
	 * @return 结果
	 */
	public static final BigDecimal round(Fld field, BigDecimal amt) {
		return amt.setScale(field.getScale(), BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 字符串合并，中间以"|"分隔
	 * 
	 * @param objects
	 * @return
	 */
	public static String strLink(Object... objects) {
		StringBuffer buf = new StringBuffer();
		for (Object s : objects)
			buf.append("|" + s.toString());
		return buf.toString().substring(1);
	}

	/**
	 * 拆以"|"分隔的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String[] strSplit(String str) {
		return str.split("|");
	}

	/**
	 * 将Byte型数据转为Boolean型
	 * @param val
	 * @return null 返回　null, 1 返回true, 其他返回 false
	 */
	public static Boolean byteToBoolean(Byte val) {
		if (val == null)
			return null;
		return val == 1 ? true : false;
	}

	/**
	 * 将Boolean型数据转为Byte型
	 * @param val
	 * @return null 返回　null, true 返回1, 其他返回 0
	 */
	public static Byte booleanToByte(Boolean val) {
		if (val == null)
			return null;
		return val ? (byte) 1 : (byte) 0;
	}

	/**
	 * @param arg0
	 * @see junit.framework.Test#run(junit.framework.TestResult)
	 */
	public final void run(TestResult arg0) {
	}

	public final void testCase() {
	}

}
