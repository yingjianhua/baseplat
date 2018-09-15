package irille.pub.svr;

import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.inf.IDb;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import oracle.sql.RAW;

/**
 * MySql数据库操作类
 * @author surface1
 * 
 */

public class DbOracle implements IDb {

	@Override
	public String crtWhereSearch(Fld fld, String param) {
		if (fld.getJavaType().equals(String.class))
			return fld.getCodeSqlField() + " like '%" + param + "%'";
		if (fld.getJavaType().equals(Date.class))
			return fld.getCodeSqlField() + " = to_date( '" + param + "','yyyy-MM-dd')";
		if (Str.isNum(param))
			return fld.getCodeSqlField() + "=" + param;
		return ""; //TODO 其它特殊类型暂不考虑
	}

	@Override
	public int getInsFirstFldId(Tb tb) {
		return 0;
	}

	@Override
	public Object toSqlObject(Fld fld, Object obj) {
		if (obj == null)
			return null;
		if (fld.getTypeName().equals("DATE"))
			obj = new java.sql.Date(((Date) obj).getTime());
		else if (fld.getTypeName().equals("TIME"))
			obj = new java.sql.Time(((Date) obj).getTime());
		return obj;
	}

	@Override
	public void setAutoPkey(Bean bean) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if (bean.gtTB().get("pkey").isRaw()) {
				String sql = "select sys_guid() from dual";
				stmt = DbPool.getInstance().getConn().prepareStatement(sql);
				rs = stmt.executeQuery();
				if (rs.next()) {
					String rawvalue = new RAW(rs.getBytes(1)).stringValue();
					stmt.setObject(1, rawvalue);
					bean.setPkeyByString(rawvalue);
				}
			} else {
				String sql = "select seq_" + ((Tb) bean.gtTB()).getCodeSqlTb() + ".nextval from dual";
				stmt = DbPool.getInstance().getConn().prepareStatement(sql);
				rs = stmt.executeQuery();
				if (rs.next()) {
					stmt.setObject(1, rs.getInt(1));
					bean.setPkeyByString(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			DbPool.getInstance().close(stmt, rs);
		}
	}

	@Override
	public String lock() {
		return ""; //未研究
	}

	@Override
	public String getPageSql(String sql, boolean lockFlag, int idx, int count) {
		if (count > 0)
			sql = "SELECT * FROM ( SELECT tt.*, rownum as no FROM ( " + sql + " ) tt ) WHERE no>" + idx + " AND no<="
			    + (idx + count);
		return sql;
	}

	@Override
	public String getConfigFile() {
		return "/dbOracle.properties";
	}

	//===============================

	public static HashMap<String, String> _oracelHm;
	{
		_oracelHm = new HashMap();
		_oracelHm.put("BYTE", "NUMBER(3,0)");
		_oracelHm.put("SHORT", "NUMBER(5,0)");
		_oracelHm.put("INT", "NUMBER(11,0)");
		_oracelHm.put("LONG", "NUMBER(20,0)");
		_oracelHm.put("DEC", "NUMBER");
		_oracelHm.put("STR", "VARCHAR");
		_oracelHm.put("STROPT", "CHAR");
		_oracelHm.put("DATE", "DATE");
		_oracelHm.put("TIME", "DATE");
		_oracelHm.put("DOUBLE", "FLOAT(24)");
		_oracelHm.put("STRINGTEXT", "NUMBER(7,0)");
		_oracelHm.put("TEXT", "VARCHAR2");
		_oracelHm.put("OPTLINE", "NUMBER(3,0)");
		_oracelHm.put("BLOB", "BLOB");
		_oracelHm.put("CLOB", "CLOB");
		_oracelHm.put("BLOBMEDIUM", "BLOB");
		_oracelHm.put("BLOBLONG", "BLOB");
		_oracelHm.put("CHAR", "CHAR");
	}

	@Override
	public HashMap<String, String> dbTypeMap() {
		return _oracelHm;
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
		// --主键--
		buf.append("PRIMARY KEY ( PKEY ),");
		buf.delete(buf.length() - 1, buf.length());
		buf.append(")");
		return buf.toString();
	}

	@Override
	public boolean hasTable(Class clazz) {
		boolean exist = false;
		String tableCode = Bean.tb(clazz).getCodeSqlTb().toUpperCase();
		try {
			Statement stmt = DbPool.getInstance().getConn().createStatement();
			ResultSet rs = stmt.executeQuery("select tname from tab where tname ='" + tableCode + "'");
			if (rs.next())
				exist = true;
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exist;
	}

	@Override
	public boolean dbIsExists(Class clazz) throws Exception {
		boolean exist = false;
		String tableCode = Bean.tb(clazz).getCodeSqlTb().toUpperCase();
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		ResultSet rs = stmt.executeQuery("select tname from tab where tname ='" + tableCode + "'");
		if (rs.next())
			exist = true;
		rs.close();
		stmt.close();
		return exist;
	}

	@Override
	public void db(Class clazz) throws Exception {
		String tbName = Bean.tb(clazz).getCodeSqlTb();
		if (!dbIsExists(clazz)) {
			Statement stmt = DbPool.getInstance().getConn().createStatement();
			stmt.execute(dbOutTableSql(clazz));
			dbCreateIndex(clazz, stmt);
			dbCreateComment(clazz, stmt);
			stmt.close();
			System.out.println("建表[" + tbName + "]-->成功!");
		} else {
			System.err.println("表[" + tbName + "]-->已存在!");
		}
		if (Bean.tb(clazz).isAutoIncrement() && dbIsSequence(clazz) == false) {
			Statement stmt = DbPool.getInstance().getConn().createStatement();
			stmt.execute(dbOutSequenceSql(clazz));
			System.out.println("建序列[seq_" + tbName + "]-->成功!");
			stmt.close();
		}
	}

	public boolean dbIsSequence(Class clazz) throws SQLException {
		// 判断--序列是否存在
		boolean exist = false;
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		ResultSet rs = stmt.executeQuery("SELECT sequence_name FROM all_sequences  where  sequence_name = 'SEQ_"
		    + Bean.tb(clazz).getCodeSqlTb().toUpperCase() + "'");
		if (rs.next()) {
			System.err.println("表[" + Bean.tb(clazz).getCodeSqlTb() + "]序列已存在");
			exist = true;
		}
		rs.close();
		return exist;

	}

	public String dbOutSequenceSql(Class clazz) {
		return "CREATE SEQUENCE seq_" + Bean.tb(clazz).getCodeSqlTb() + " INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE";
	}

	public String dbOutTriggerSql(Class clazz) {
		return "CREATE OR REPLACE TRIGGER trg_" + Bean.tb(clazz).getCodeSqlTb() + " BEFORE INSERT ON "
		    + Bean.tb(clazz).getCodeSqlTb() + " REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW begin SELECT seq_"
		    + Bean.tb(clazz).getCodeSqlTb() + ".nextval INTO :new.pkey FROM dual; end;";
	}

	public void dbCreateComment(Class clazz, Statement stmt) throws SQLException {
		Tb tb = Bean.tb(clazz);
		stmt.execute("COMMENT ON TABLE " + tb.getCodeSqlTb() + " IS '" + tb.getName() + "'");
		for (Fld fld : tb.getFlds()) {
			if (!fld.isDatabaseField())
				continue;
			dbOutFldSql(fld);
			stmt.execute("COMMENT ON COLUMN " + tb.getCodeSqlTb() + "." + fld.getCodeSqlField() + " IS '" + fld.getName()
			    + "'");
		}
	}

	public void dbCreateIndex(Class clazz, Statement stmt) throws SQLException {
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
				sql += "INDEX idx_" + tb.getCodeSqlTb() + "_" + index.getCodeSql() + " ON " + tb.getCodeSqlTb() + "(";
				for (int m = 0; m < index.getFields().length; m++) {
					if (index.getFields()[m].isIndexAscending())
						sql += index.getFields()[m].getCodeSqlField() + " ASC";
					else
						sql += index.getFields()[m].getCodeSqlField() + " DESC";
					if (m < index.getFields().length - 1)
						sql += ", ";
				}
				sql += ")";
				stmt.execute(sql);
			}
		}
	}
}
