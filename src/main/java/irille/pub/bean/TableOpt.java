//Created on 2005-11-14
package irille.pub.bean;

import irille.pub.Log;
import irille.pub.svr.DbPool;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class TableOpt {
	private static final Log LOG = new Log(Fld.class);

	public static HashMap<String, String> _mysqlHm;
	public static Bean _model;
	public Connection _conn = DbPool.getInstance().getConn();
	{
		_mysqlHm = new HashMap();
		_mysqlHm.put("BYTE", "TINYINT");
		_mysqlHm.put("SHORT", "SMALLINT");
		_mysqlHm.put("INT", "INT");
		_mysqlHm.put("LONG", "BIGINT");
		_mysqlHm.put("DEC", "DECIMAL");
		_mysqlHm.put("STR", "VARCHAR");
		_mysqlHm.put("DATE", "DATE");
		_mysqlHm.put("TIME", "DATETIME");
		_mysqlHm.put("TIMESTAMP", "DATETIME");
		_mysqlHm.put("DOUBLE", "DOUBLE");
		_mysqlHm.put("STRINGTEXT", "MEDIUMTEXT");
		_mysqlHm.put("TEXT", "TEXT");
		_mysqlHm.put("OPTLINE", "TINYINT");
		_mysqlHm.put("BLOB", "BLOB");
		_mysqlHm.put("CLOB", "CLOB");
		_mysqlHm.put("BLOBMEDIUM", "MEDIUMBLOB");
		_mysqlHm.put("BLOBLONG", "LONGBLOB");
		// JavaType SqlType TypeName
		// -----------------------------------------------------
		// Byte.class Types.TINYINT BYTE
		// Short.class Types.SMALLINT SHORT
		// Integer.class Types.INTEGER INT
		// Long.class Types.BIGINT LONG
		// BigDecimal.class Types.DECIMAL DEC
		// String.class Types.VARCHAR STR
		// Date.class Types.DATE DATE
		// Date.class Types.TIME TIME
	}

	public static String outFldSql(Fld fld) {
		StringBuilder buf = new StringBuilder();
		buf.append(fld.getCodeSqlField() + " ");
		String type = _mysqlHm.get(fld.getTypeName());
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
		if (fld.getCode().equals("key") && ((Tb)fld.getTb()).isAutoIncrement()) // 自增
			buf.append(" auto_increment");
		buf.append(",");
		return buf.toString();
	}

	// 判断表是否存在
	public boolean isExists(Class clazz) throws Exception {
		String tableCode = Bean.tb(clazz).getCodeSqlTb();
		try {
			DatabaseMetaData dbmd = _conn.getMetaData();
			ResultSet rs = dbmd.getTables(null, null, tableCode, null); // 检索表名为table_code的信息
			if (rs.next()) { // 判断存在相同的表名
				rs.close();
				return true; // 返回true表示存在
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void createAndNotDel(Class clazz) throws Exception {
		String tbName = Bean.tb(clazz).getCodeSqlTb();
		if (!isExists(clazz)) {
			Statement stmt = _conn.createStatement();
			stmt.execute(outTableSql(clazz));
			System.out.println("建表[" + tbName + "]-->成功!");
			return;
		}
		System.err.println("表[" + tbName + "]-->已存在!");
	}

	public String outTableSql(Class clazz) throws Exception {
		Tb tb = Bean.tb(clazz);
		StringBuffer buf = new StringBuffer();
		buf.append("CREATE TABLE IF NOT EXISTS " + tb.getCodeSqlTb() + "(");
		for (Fld fld : tb.getFlds()) {
			if (!fld.isDatabaseField())
				continue;
			buf.append(outFldSql(fld));
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
					buf.append(index.getFields()[m].getCodeSqlField() + ",");
				}
				buf.delete(buf.length() - 1, buf.length());
				buf.append("),");
			}
		}
		buf.delete(buf.length() - 1, buf.length());
		buf.append(") " + "ENGINE=InnoDB DEFAULT CHARSET=utf8");
		return buf.toString();
	}

	//
	// /**
	// * 取mysql的字段类型
	// */
	// public static String getMysqlType(String type_code) {
	// Iterator key = _mysqlHm.keySet().iterator();
	// Object ob = null;
	// while (key.hasNext()) {
	// ob = key.next();
	// if (ob.toString().equals(type_code.toUpperCase().trim())) {
	// return _mysqlHm.get(ob).toString();
	// }
	// }
	// System.err.println(type_code);
	// return null;
	// }
	//
	// /**
	// * 取FRKJ的字段类型
	// * @param type
	// * @return
	// */
	// public static String getFrkjType(TypeBase type) {
	// if (type instanceof TypeText)
	// return type.getHBMType();
	// if (type instanceof TypeTime)
	// return "TIME";
	// String typeCode = Str
	// .getSplitLastCode(type.getJavaTypeClass().toString(), ".");
	// return typeCode;
	// }

	public static String createFieldSingle(Fld fld) {
		String sql = outFldSql(fld);
		return sql.substring(0, sql.length() - 1);
	}

	public static boolean alterModify(Statement stmt, String tableName, Fld fld) throws Exception {
		return stmt.execute("ALTER TABLE " + tableName + " MODIFY " + createFieldSingle(fld));
	}

	public static boolean alterDrop(Statement stmt, String tableName, String columnName)
	    throws Exception {
		return stmt.execute("ALTER TABLE " + tableName + " DROP " + columnName);
	}

	public static boolean alterAdd(Statement stmt, String tableName, Fld fld) throws Exception {
		System.err.println(createFieldSingle(fld));
		return stmt.execute("ALTER TABLE " + tableName + " ADD " + createFieldSingle(fld));
	}

}
