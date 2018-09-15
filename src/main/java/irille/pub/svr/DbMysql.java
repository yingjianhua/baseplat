package irille.pub.svr;

import irille.core.sys.Sys;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.inf.IDb;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * MySql数据库操作类
 * @author surface1
 *
 */

public class DbMysql implements IDb {

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
		return ""; //TODO 其它特殊类型暂不考虑
	}

	@Override
	public int getInsFirstFldId(Tb tb) {
		if (tb.isAutoIncrement())
			return 1;
		return 0;
	}

	@Override
	public Object toSqlObject(Fld fld, Object obj) {
		return obj;
	}

	@Override
	public void setAutoPkey(Bean bean) throws Exception {
	}

	@Override
	public String lock() {
		return " FOR UPDATE";
	}

	@Override
	public String getPageSql(String sql, boolean lockFlag, int idx, int count) {
		if (count > 0)
			sql += " LIMIT " + idx + "," + count;
		if (lockFlag)
			sql += lock();
		return sql;
	}

	@Override
	public String getConfigFile() {
		return "/dbMysql.properties";
	}

	//===============================

	public static HashMap<String, String> _mysqlHm;
	{
		_mysqlHm = new HashMap();
		_mysqlHm.put("BYTE", "TINYINT");
		_mysqlHm.put("BOOLEAN", "TINYINT");
		_mysqlHm.put("SHORT", "SMALLINT");
		_mysqlHm.put("INT", "INT");
		_mysqlHm.put("LONG", "BIGINT");
		_mysqlHm.put("DEC", "DECIMAL");
		_mysqlHm.put("STR", "VARCHAR");
		_mysqlHm.put("DATE", "DATE");
		_mysqlHm.put("TIME", "DATETIME");
		_mysqlHm.put("DOUBLE", "DOUBLE");
		_mysqlHm.put("STRINGTEXT", "MEDIUMTEXT");
		_mysqlHm.put("TEXT", "TEXT");
		_mysqlHm.put("OPTLINE", "TINYINT");
		_mysqlHm.put("BLOB", "TEXT");
		_mysqlHm.put("CLOB", "TEXT");
		_mysqlHm.put("CHAR", "CHAR");
		_mysqlHm.put("STROPT", "CHAR");
		_mysqlHm.put("JSONOBJECT", "JSON");
		_mysqlHm.put("JSONARRAY", "JSON");
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
			buf.append("(" + fld.getLen());
			if (fld.getScale() > 0)
				buf.append("," + fld.getScale());
			buf.append(")");
		}
		// 空值标志
		if (!fld.isNull())
			buf.append(" NOT");
		buf.append(" NULL");
		if (fld.getCode().equals("pkey") && ((Tb) fld.getTb()).isAutoIncrement()) // 自增
			buf.append(" auto_increment");
		buf.append(",");
		return buf.toString();
	}

	@Override
	public String dbOutTableSql(Class clazz) throws Exception {
		Tb tb = Bean.tb(clazz);
		StringBuffer buf = new StringBuffer();
		buf.append("CREATE TABLE IF NOT EXISTS " + tb.getCodeSqlTb() + "(");
		for (Fld fld : tb.getFlds()) {
			if (!fld.isDatabaseField())
				continue;
			buf.append(dbOutFldSql(fld));
		}
		// --主键--
		buf.append("PRIMARY KEY ( PKEY ),");
		// --索引--
		if (tb.getIndexes() != null) {
			Iterator i = tb.getIndexes().iterator();
			Index index;
			while (i.hasNext()) {
				index = (Index) i.next();
				if (index.isUnique())
					buf.append("UNIQUE ");
				buf.append("INDEX (");
				for (int m = 0; m < index.getFields().length; m++) {
					if (index.getFields()[m].isIndexAscending())
						buf.append(index.getFields()[m].getCodeSqlField() + " ASC,");
					else
						buf.append(index.getFields()[m].getCodeSqlField() + " DESC ,");
				}
				buf.delete(buf.length() - 1, buf.length());
				buf.append("),");
			}
		}
		buf.delete(buf.length() - 1, buf.length());
		buf.append(") " + "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
		return buf.toString();
	}

	@Override
	public boolean hasTable(Class clazz) {
		String tableCode = Bean.tb(clazz).getCodeSqlTb();
		try {
			DatabaseMetaData dbmd = DbPool.getInstance().getConn().getMetaData();
			ResultSet rs = dbmd.getTables(null, null, tableCode, null); // 检索表名为table_code的信息
			if (rs.next()) { // 判断存在相同的表名
				rs.close();
				return true;
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean dbIsExists(Class clazz) throws Exception {
		String tableCode = Bean.tb(clazz).getCodeSqlTb();
		try {
			DatabaseMetaData dbmd = DbPool.getInstance().getConn().getMetaData();
			ResultSet rs = dbmd.getTables(null, null, tableCode, null); // 检索表名为table_code的信息
			if (rs.next()) { // 判断存在相同的表名
				rs.close();
				String sql = "SELECT count(*) FROM " + Bean.tb(clazz).getCodeSqlTb();
				int count = ((Number) BeanBase.queryOneRow(sql)[0]).intValue();
				if (count > 0)
					return true;
				else {
					Statement stmt = DbPool.getInstance().getConn().createStatement();
					stmt.execute("DROP TABLE " + Bean.tb(clazz).getCodeSqlTb());
					System.out.println("删除表[" + Bean.tb(clazz).getCodeSqlTb() + "]-->成功!");
					stmt.close();
					return false;
				}
			}
			rs.close();
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
			System.out.println("建表[" + tbName + "]-->成功!");
			return;
		}
		System.err.println("表[" + tbName + "]-->已存在!");
	}
}
