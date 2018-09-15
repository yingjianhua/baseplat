package irille.pub.inf;

import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import java.util.HashMap;

public interface IDb {
	public static final Log LOG = new Log(IDb.class);

	/**
	 * 建SQL查询条件
	 * @param fld
	 * @param param 前EXT返回的查询值
	 * @return
	 */
	public String crtWhereSearch(Fld fld, String param);

	/**
	 * 取新增时，PreparedStatement需要设置的第1个下标
	 * 主要注意ORCAL时，自增型也是需要从主键开始设置
	 * @param tb
	 * @return
	 */
	public int getInsFirstFldId(Tb tb);

	/**
	 * 向预处理对象中设置值时，不同数据库对类型要求不一致
	 * @param fld
	 * @param obj
	 * @return
	 */
	public Object toSqlObject(Fld fld, Object obj);

	/**
	 * 自增长或系统设置的主键，特殊处理
	 * @param bean
	 */
	public void setAutoPkey(Bean bean) throws Exception;

	/**
	 * 取行锁定方式
	 * @return
	 */
	public String lock();

	public String getPageSql(String sql, boolean lockFlag, int idx, int count);
	
	/**
	 * 取主键
	 * 及其他特定的方法
	 */
	public String getConfigFile();

	//以下建表相关==================================

	/**
	 * 取JAVA类型与数据库的映射关系
	 * @return
	 */
	public HashMap<String, String> dbTypeMap();

	/**
	 * 根据FLD产生数据库建表时的字段定义值
	 * @param fld
	 * @return
	 */
	public String dbOutFldSql(Fld fld);

	/**
	 * 根据CLAZZ产生建表的SQL语句
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public String dbOutTableSql(Class clazz) throws Exception;

	/**
	 * 判断数据中当表表是否是存在
	 * 注意如果表中无记录，也作为不存在处理
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public boolean dbIsExists(Class clazz) throws Exception;

	/**
	 * 执行数据建表操作
	 * @param clazz
	 * @throws Exception
	 */
	public void db(Class clazz) throws Exception;

	//仅判断是否存在表
	public boolean hasTable(Class clazz);

	// 以下更改的相关语句，待参考===============================
	//	public static String createFieldSingle(Fld fld) {
	//		String sql = outFldSql(fld);
	//		return sql.substring(0, sql.length() - 1);
	//	}
	//
	//	public static boolean alterModify(Statement stmt, String tableName, Fld fld)
	//	    throws Exception {
	//		return stmt.execute("ALTER TABLE " + tableName + " MODIFY "
	//		    + createFieldSingle(fld));
	//	}
	//
	//	public static boolean alterDrop(Statement stmt, String tableName,
	//	    String columnName) throws Exception {
	//		return stmt.execute("ALTER TABLE " + tableName + " DROP " + columnName);
	//	}
	//
	//	public static boolean alterAdd(Statement stmt, String tableName, Fld fld)
	//	    throws Exception {
	//		System.err.println(createFieldSingle(fld));
	//		return stmt.execute("ALTER TABLE " + tableName + " ADD "
	//		    + createFieldSingle(fld));
	//	}
}
