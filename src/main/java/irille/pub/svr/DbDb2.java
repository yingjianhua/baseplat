package irille.pub.svr;

import irille.core.sys.Sys;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanLong;
import irille.pub.inf.IDb;
import irille.pub.tb.Fld;
import irille.pub.tb.FldStr;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 更改用户特权
 * 新建 8K缓冲池、新建8K表空间 - IRILLESPACE
 * 新建 模式-IRILLEMS
 * 
 * count(*) 中不允许出现order by
 * @author whx
 * @version 创建时间：2015年9月2日 上午9:56:17
 */
public class DbDb2 implements IDb {
	public static final String SEQ = "SEQ_";
	public static final String MS = "IRILLEMS"; //当前模式 在配置文件的URL中使用了，这里没有引用
	public static final String SPACE = "IRILLESPACE";

	public String crtWhereSearch(Fld fld, String param) {
		String[] params = param.split("##");
		if (params.length < 2) { //普通搜索需要加入符号条件
			if (fld.getJavaType().equals(String.class))
				param = Sys.OOptCht.INC.getLine().getKey() + "##" + param;
			else
				param = Sys.OOptCht.EQU.getLine().getKey() + "##" + param;
			params = param.split("##");
		}
		if (Str.value(Sys.OOptCht.EMP).equals(params[0]))
			return fld.getCodeSqlField() + " IS NULL";
		if (Str.value(Sys.OOptCht.NOEMP).equals(params[0]))
			return fld.getCodeSqlField() + " IS NOT NULL";
		if (fld.getJavaType().equals(String.class)) {
			if (Str.value(Sys.OOptCht.INC).equals(params[0]))
				return fld.getCodeSqlField() + " like '%" + params[1] + "%'";
			if (Str.value(Sys.OOptCht.ST).equals(params[0]))
				return fld.getCodeSqlField() + " like '" + params[1] + "%'";
			if (Str.value(Sys.OOptCht.EQU).equals(params[0]))
				return fld.getCodeSqlField() + " = '" + params[1] + "'";
			if (Str.value(Sys.OOptCht.NOEQU).equals(params[0]))
				return fld.getCodeSqlField() + " <> '" + params[1] + "'";
		}
		if (fld.getJavaType().equals(Date.class)) {
			if (Str.value(Sys.OOptCht.EQU).equals(params[0]))
				return fld.getCodeSqlField() + " = '" + params[1] + "'";
			if (Str.value(Sys.OOptCht.NOEQU).equals(params[0]))
				return fld.getCodeSqlField() + " <> '" + params[1] + "'";
			if (Str.value(Sys.OOptCht.GT).equals(params[0]))
				return fld.getCodeSqlField() + " > '" + params[1] + "'";
			if (Str.value(Sys.OOptCht.LT).equals(params[0]))
				return fld.getCodeSqlField() + " < '" + params[1] + "'";
			if (Str.value(Sys.OOptCht.GE).equals(params[0]))
				return fld.getCodeSqlField() + " >= '" + params[1] + "'";
			if (Str.value(Sys.OOptCht.LE).equals(params[0]))
				return fld.getCodeSqlField() + " <= '" + params[1] + "'";
			if (Str.value(Sys.OOptCht.BTW).equals(params[0])) {
				if (params.length < 3)
					return fld.getCodeSqlField() + " >= '" + params[1] + "'";
				if (Str.isEmpty(params[1]))
					return fld.getCodeSqlField() + " <= '" + params[2] + "'";
				return fld.getCodeSqlField() + " >= '" + params[1] + "' AND " + fld.getCodeSqlField() + " <= '" + params[2]
				    + "'";
			}
		}
		if (Str.isNum(params[1])) {
			if (Str.value(Sys.OOptCht.EQU).equals(params[0]))
				return fld.getCodeSqlField() + " = " + params[1];
			if (Str.value(Sys.OOptCht.NOEQU).equals(params[0]))
				return fld.getCodeSqlField() + " <> " + params[1];
			if (Str.value(Sys.OOptCht.GT).equals(params[0]))
				return fld.getCodeSqlField() + " > " + params[1];
			if (Str.value(Sys.OOptCht.LT).equals(params[0]))
				return fld.getCodeSqlField() + " < " + params[1];
			if (Str.value(Sys.OOptCht.GE).equals(params[0]))
				return fld.getCodeSqlField() + " >= " + params[1];
			if (Str.value(Sys.OOptCht.LE).equals(params[0]))
				return fld.getCodeSqlField() + " <= " + params[1];
		}
		return "";
	}

	@Override
	public int getInsFirstFldId(Tb tb) {
		return 0;
	}

	@Override
	public Object toSqlObject(Fld fld, Object obj) {
		return obj;
	}

	@Override
	public void setAutoPkey(Bean bean) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "VALUES NEXTVAL FOR " + SEQ + bean.gtTB().getCodeSqlTb().toUpperCase();
			stmt = DbPool.getInstance().getConn().createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) { // 知其仅有一 列，故获取第一列
				bean.setPkeyByString(rs.getString(1));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			DbPool.getInstance().close(stmt, rs);
		}
	}

	@Override
	public String lock() {
		return " FOR UPDATE WITH RS";
	}

	@Override
	public String getPageSql(String sql, boolean lockFlag, int idx, int count) {
		if (count > 0)
			sql = "SELECT * FROM ( SELECT tt.*, ROWNUMBER() OVER() AS RN FROM ( " + sql + " ) tt ) WHERE RN>" + idx
			    + " AND RN<=" + (idx + count);
		else if (lockFlag) //分页查询的情况下，行锁会出错
			sql += lock();
		return sql;
	}

	@Override
	public String getConfigFile() {
		return "/dbDb2.properties";
	}

	//===============================

	public static HashMap<String, String> _mysqlHm;
	{
		_mysqlHm = new HashMap();
		_mysqlHm.put("BYTE", "SMALLINT");
		_mysqlHm.put("BOOLEAN", "SMALLINT");
		_mysqlHm.put("SHORT", "SMALLINT");
		_mysqlHm.put("INT", "INTEGER");
		_mysqlHm.put("LONG", "BIGINT");
		_mysqlHm.put("DEC", "DECIMAL");
		_mysqlHm.put("STR", "VARCHAR");
		_mysqlHm.put("DATE", "DATE");
		_mysqlHm.put("TIME", "TIMESTAMP");
		_mysqlHm.put("DOUBLE", "DOUBLE");
		_mysqlHm.put("STRINGTEXT", "CLOB(16777216) INLINE LENGTH 196");
		_mysqlHm.put("TEXT", "CLOB(1048576) INLINE LENGTH 164");
		_mysqlHm.put("OPTLINE", "SMALLINT");
		_mysqlHm.put("BLOB", "CLOB(1048576) INLINE LENGTH 164");
		_mysqlHm.put("CLOB", "CLOB(1048576) INLINE LENGTH 164");
		_mysqlHm.put("CHAR", "CHAR");
		_mysqlHm.put("STROPT", "CHAR");
	}

	@Override
	public HashMap<String, String> dbTypeMap() {
		return _mysqlHm;
	}

	@Override
	public String dbOutFldSql(Fld fld) {
		StringBuilder buf = new StringBuilder();
		buf.append(fld.getCodeSqlField() + " ");
		String type = dbTypeMap().get(fld.getTypeName());
		if (type == null)
			throw LOG.err("typeNameInvalid", "数据类型[{0}]无效!", fld.getTypeName());
		buf.append(type);
		if (fld.getLen() > 0) {
			int len = fld.getLen();
			if (fld.getClass().equals(FldStr.class))
				len = len * 3;
			buf.append("(" + len);
			if (fld.getScale() > 0)
				buf.append("," + fld.getScale());
			buf.append(")");
		}
		// 空值标志
		if (!fld.isNull())
			buf.append(" NOT NULL");
		buf.append(",");
		return buf.toString();
	}

	@Override
	public String dbOutTableSql(Class clazz) throws Exception {
		Tb tb = Bean.tb(clazz);
		StringBuffer buf = new StringBuffer();
		buf.append("CREATE TABLE " + tb.getCodeSqlTb() + "(");
		for (Fld fld : tb.getFlds()) {
			if (!fld.isDatabaseField())
				continue;
			buf.append(dbOutFldSql(fld));
		}
		buf.delete(buf.length() - 1, buf.length());
		buf.append(")");
		buf.append(" DATA CAPTURE NONE");
		buf.append(" IN \"" + SPACE + "\""); //默认表空间
		buf.append(" COMPRESS NO");
		return buf.toString();
	}

	@Override
	public boolean hasTable(Class clazz) {
		boolean exist = false;
		String tableCode = Bean.tb(clazz).getCodeSqlTb().toUpperCase();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbPool.getInstance().getConn().createStatement();
			rs = stmt.executeQuery("select tabname from syscat.tables where tabname= '" + tableCode.toUpperCase() + "'");
			if (rs.next())
				exist = true;
		} catch (Exception e) {
			throw LOG.err("hasTable", "判断表是否存在出错:", tableCode);
		} finally {
			DbPool.getInstance().close(stmt, rs);
		}
		return exist;
	}

	@Override
	public boolean dbIsExists(Class clazz) throws Exception {
		try {
			if (hasTable(clazz)) { // 判断存在相同的表名
				String sql = "SELECT count(*) FROM " + Bean.tb(clazz).getCodeSqlTb();
				int count = ((Number) BeanBase.queryOneRow(sql)[0]).intValue();
				if (count > 0)
					return true;
				else {
					dbDrop(clazz);
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void db(Class clazz) throws Exception {
		String tbName = Bean.tb(clazz).getCodeSqlTb();
		if (!dbIsExists(clazz)) {
			Statement stmt = DbPool.getInstance().getConn().createStatement();
			stmt.execute(dbOutTableSql(clazz));
			dbCreatePkey(clazz);
			dbCreateIndex(clazz);
			dbCreateSeq(clazz);
			//建索引
			System.out.println("建表[" + tbName + "]-->成功!");
			DbPool.getInstance().getConn().commit();
			DbPool.getInstance().close(stmt);
			return;
		}
		System.err.println("表[" + tbName + "]-->已存在!");
	}

	private void dbCreatePkey(Class clazz) throws Exception {
		String tableCode = Bean.tb(clazz).getCodeSqlTb().toUpperCase();
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("ALTER TABLE " + tableCode + " ADD CONSTRAINT " + tableCode + "_PK PRIMARY KEY(pkey)");
		DbPool.getInstance().close(stmt);
	}

	private void dbCreateIndex(Class clazz) throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		Tb tb = Bean.tb(clazz);
		if (tb.getIndexes() != null) {
			String sql = "";
			Iterator i = tb.getIndexes().iterator();
			Index index;
			while (i.hasNext()) {
				sql = "CREATE ";
				index = (Index) i.next();
				if (index.isUnique())
					sql += "UNIQUE ";
				sql += "INDEX " + tb.getCodeSqlTb() + "_IDX_" + index.getCodeSql() + " ON " + tb.getCodeSqlTb() + "(";
				for (int m = 0; m < index.getFields().length; m++) {
					if (index.getFields()[m].isIndexAscending())
						sql += index.getFields()[m].getCodeSqlField() + " ASC";
					else
						sql += index.getFields()[m].getCodeSqlField() + " DESC";
					if (m < index.getFields().length - 1)
						sql += ", ";
				}
				sql += ")";
				sql += " MINPCTUSED 0";
				sql += " ALLOW REVERSE SCANS";
				sql += " PAGE SPLIT SYMMETRIC";
				sql += " COLLECT SAMPLED DETAILED STATISTICS";
				sql += " COMPRESS NO";
				stmt.execute(sql.toUpperCase());
			}
		}
		DbPool.getInstance().close(stmt);
	}

	//建序列
	private void dbCreateSeq(Class clazz) throws Exception {
		Tb tb = Bean.tb(clazz);
		if (tb.isAutoIncrement() == false)
			return;
		String type = "INTEGER";
		if (BeanLong.class.isAssignableFrom(clazz))
			type = "BIGINT";
		String tableCode = Bean.tb(clazz).getCodeSqlTb().toUpperCase();
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("CREATE SEQUENCE " + SEQ + tableCode + " AS " + type
		    + " START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE NO CYCLE NO ORDER CACHE 20");
		DbPool.getInstance().close(stmt);
	}

	//判断序列是否存在
	private boolean hasSeq(Class clazz) throws Exception {
		String tableCode = Bean.tb(clazz).getCodeSqlTb().toUpperCase();
		String sql = "SELECT count(*) FROM SYSCAT.SEQUENCES WHERE SEQNAME='" + SEQ + tableCode + "'";
		int count = ((Number) BeanBase.queryOneRow(sql)[0]).intValue();
		if (count > 0)
			return true;
		return false;
	}

	//删除主键、索引、表
	private void dbDrop(Class clazz) throws Exception {
		Tb tb = Bean.tb(clazz);
		String tableCode = Bean.tb(clazz).getCodeSqlTb().toUpperCase();
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("ALTER TABLE " + tableCode + " DROP CONSTRAINT " + tableCode + "_PK");
		if (tb.getIndexes() != null) {
			String sql = "";
			Iterator i = tb.getIndexes().iterator();
			Index index;
			while (i.hasNext()) {
				index = (Index) i.next();
				stmt.execute("DROP INDEX " + tableCode + "_IDX_" + index.getCodeSql().toUpperCase());
			}
		}
		stmt.execute("DROP TABLE " + tableCode);
		if (hasSeq(clazz))
			stmt.execute("DROP SEQUENCE " + SEQ + tableCode);
		DbPool.getInstance().close(stmt);
		System.out.println("删除表[" + Bean.tb(clazz).getCodeSqlTb() + "]-->成功!");
	}

}
