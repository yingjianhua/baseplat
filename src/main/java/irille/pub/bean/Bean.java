package irille.pub.bean;

import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;

import irille.core.sys.SysTable;
import irille.pub.ClassTools;
import irille.pub.Exp;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.Str;
import irille.pub.dep.BeanSrc;
import irille.pub.svr.DbPool;
import irille.pub.svr.Env;
import irille.pub.svr.Env.SysConf;
import irille.pub.svr.ISvrVars;
import irille.pub.tb.Fld;
import irille.pub.tb.Infs.ITbOutKey;
import irille.pub.tb.Opt;
import irille.pub.tb.Tb;
import irille.pub.view.Fmts;
import oracle.sql.RAW;
import oracle.sql.TIMESTAMP;

/**
 * Title: 数据模型基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public abstract class Bean<BEAN extends Bean, KEY extends Serializable> extends BeanBase<BEAN, KEY> implements ISvrVars {
	private static final Log LOG = new Log(Bean.class);

	public static final String MAIN_PKEY = "mainPkey";

	public static final int QTY_DIGITS = Env.getConfParaInt(SysConf.QTY_DIGITS);
	public static final int PRICE_DIGITS = Env.getConfParaInt(SysConf.PRICE_DIGITS);
	public static final int AMT_DIGITS = Env.getConfParaInt(SysConf.AMT_DIGITS);

	// 记录状态
	public static final byte IDUS_INIT = 1;
	public static final byte IDUS_INS = 2;
	public static final byte IDUS_DEL = 3;
	public static final byte IDUS_UPD = 4;
	public static final byte IDUS_SELECT = 5;
	public static final Opt IDUS = new Opt(LOG, "idus", "增删改选").add(IDUS_INIT, "初始").add(IDUS_INS, "新增")
	    .add(IDUS_DEL, "删除").add(IDUS_UPD, "修改").add(IDUS_SELECT, "选择");

	// 记录的状态，常用于前台对多记录的操作
	private byte _idus = IDUS_INIT;

	/**
	 * 根据表的主健及主键算法取对象
	 * @param tbObj
	 * @return
	 */
	public static Bean gtLongTbObj(Long tbObj) {
		int id = (int) (tbObj % SysTable.NUM_BASE);
		Class clazz = Tb.getTableClass(id);
		return BeanBase.load(clazz, tbObj / SysTable.NUM_BASE);
	}

	public static Class gtLongClass(Long tbObj) {
		int id = (int) (tbObj % SysTable.NUM_BASE);
		return Tb.getTableClass(id);
	}

	public final Long gtLongPkey() {
		String pkey = getPkey().toString();
		if (Str.isNum(pkey) == false)
			throw LOG.err("notNum", "[{0}]主键非数字类型，不可附加表ID值", gtTB().getName());
		return new Long(Long.parseLong(pkey) * SysTable.NUM_BASE + gtTbId());
	}

	public static final Long gtLongPkey(Integer pkey, Integer tbId) {
		return new Long(pkey * SysTable.NUM_BASE + tbId);
	}

	public static final Long gtLongPkey(Long pkey, Integer tbId) {
		return new Long(pkey * SysTable.NUM_BASE + tbId);
	}

	/**
	 * 对象初始化， 在子类中根据需要进行重构，以便DAO中调用
	 */
	public BEAN init() {
		return (BEAN) this;
	}

	/**
	 * 取记录状态
	 * 
	 * @return
	 */
	@JsonIgnore
	public final byte getIDUS() {
		return _idus;
	}

	/**
	 * 值记录的状态
	 * 
	 * @param idus
	 */
	public final void setIDUS(byte idus) {
		_idus = idus;
	}

	@Override
	public final Tb gtTB() {
		return Tb.getTBByBean(getClass());
	}

	public final int gtTbId() {
		return SysTable.gtTable(getClass()).getPkey();
	}

	/**
	 * 返回Bean的字符串表示
	 * 
	 * @return 结果
	 */
	public String toString() {
		return toStringAddHead(msg());
	}

	/**
	 * 合法性检查，在子类中根据需要进行重构，以便DAO中调用
	 * 
	 * @param m
	 */
	public void validate() {
	}

	/**
	 * 取记录的字符串表示
	 * 
	 * @return
	 */
	public abstract String msg();

	/**
	 * 取主键值列表
	 * 
	 * @return
	 */
	public abstract Serializable[] pkeyValues();

	/**
	 * 置主键的值
	 * 
	 * @param values
	 */
	public abstract void setPkeyValues(Serializable... values);

	/**
	 * 输出主键的SQL WHERE 表达式表示
	 * 
	 * @return
	 */
	public abstract String outPkeyWhereSql();

	/**
	 * 置主键的值，参数类型为String
	 * 
	 * @param pkeyValue
	 */
	public abstract void setPkeyByString(String pkeyValue);

	/**
	 * 转为字符串，格式如：“1111:总机构”
	 * 
	 * @param modelCn
	 *          Cn
	 * @param fields
	 *          字段
	 * @return 结果
	 */
	protected String toStringFieldValues(Fld... fields) {

		StringBuilder buf = new StringBuilder();
		Object value;
		for (Fld field : fields) {
			value = ClassTools.getProperty(this, field.getCode());
			if (value != null && value instanceof BigDecimal)
				buf.append(":" + Fmts.FMT_NUM.out(null, value));
			else if (field.getOpt() != null)
				buf.append(":" + field.getOpt().get(value.toString()).getName());
			else
				buf.append(":" + value);
		}
		return buf.toString().substring(1);
	}

	/**
	 * 将字符串前加模型名，如str的值为“张三”，则结果类似：“用户[张三]”
	 * 
	 * @param str
	 *          字符串
	 * @return
	 */
	protected String toStringAddHead(String str) {
		return gtTB().getName() + "[" + str + "]";
	}

	/**
	 * 取名称
	 * 
	 * @return
	 */
	public String typeName() {
		return gtTB().getName();
	}

	/**
	 * 转为字符串，格式如:"代码=1111，名称=总机构"
	 * 
	 * @param modelCn
	 *          Cn
	 * @param fields
	 *          字段
	 * @return 结果
	 */
	protected String toStringFieldValueAndNames(Fld... fields) {
		StringBuilder buf = new StringBuilder();
		Object value;
		for (Fld field : fields) {
			value = ClassTools.getProperty(this, field.getCode());
			if (value != null && value instanceof BigDecimal)
				buf.append("," + field.getName() + "=[" + Fmts.FMT_NUM.out(null, value) + "]");
			else if (field.getOpt() != null)
				buf.append("," + field.getName() + "=[" + field.getOpt().get(value.toString()).getName() + "]");
			else if (value instanceof BeanMain) {
				buf.append("," + field.getName() + "=[" + ((BeanMain) value).msg() + "]");
			} else
				buf.append("," + field.getName() + "=[" + value + "]");
		}
		return buf.toString().substring(1);
	}

	/**
	 * 转为字符串，格式如：1111:机构1
	 * 
	 * @param fields
	 *          字段
	 * @return 结果
	 */
	protected String toStringCodeName() {
		return ClassTools.getProperty(this, "code") + ":" + ClassTools.getProperty(this, "name");
	}

	public static boolean checkScope(int negativeZeroPlus, BigDecimal val) {
		if (negativeZeroPlus != 0 && negativeZeroPlus != 1 && negativeZeroPlus != 10 && negativeZeroPlus != 11
		    && negativeZeroPlus != 100 && negativeZeroPlus != 101 && negativeZeroPlus != 110 && negativeZeroPlus != 111)
			throw LOG.err("scope", "有效范围的值[{0}]无效", negativeZeroPlus);
		byte[] bytes = ("" + (1000 + negativeZeroPlus)).substring(1).getBytes();
		int result;
		result = val.signum();
		return !(result < 0 && bytes[0] != '1' || result == 0 && bytes[1] != '1' || result > 0 && bytes[2] != '1');
	}

	/**
	 * @return
	 * @see irille.svr.pub.ModelBaseInf#clazzCode()
	 */
	public final String clazzCode() {
		String name = getClass().getName();
		if (name.indexOf("$$") >= 0)
			return name.substring(0, name.indexOf("$$"));
		return name;
	}

	public final String clazzLastCode() {
		return Str.getClazzLastCode(getClass());
	}

	@Override
	public Class clazz() {
		return getClass();
	}

	/**
	 * 将主键值转换成字符串，如：pkey1=111,pkey2="AA"
	 * 
	 * @param pkeys
	 * @return
	 */
	public abstract String pkeysToString(Serializable... pkeys);

	/**
	 * @return
	 * @see junit.framework.Test#countTestCases()
	 */
	@Test
	public void countTestCases() {
		new BeanSrc(this).outSrc();
	}

	/**
	 * 新增记录 d
	 * 
	 * @return 如果是主键为自增长类型，则返回新增记录的主键值，否则返回0
	 */
	public BEAN ins() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int id = 0;
		Tb tb = (Tb) gtTB();
		try {
			chkVersion();
			stmt = DbPool.getInstance().getConn().prepareStatement(tb.getSqlIns(), Statement.RETURN_GENERATED_KEYS);
			int firstFldId = Env.INST.getDB().getInsFirstFldId(tb);
			if (tb.isAutoIncrement())
				Env.INST.getDB().setAutoPkey(this);
			toPreparedStatementBean(stmt, firstFldId, true, tb.getFlds());
			stmt.executeUpdate();
			if (tb.isAutoIncrement() && (Env.INST.getDB() instanceof irille.pub.svr.DbMysql)) {// 取自增字段的值赋回对象
				// 取自增字段的值赋回对象
				// mysql 赋主键
				rs = stmt.getGeneratedKeys();
				if (rs.next()) { // 知其仅有一 列，故获取第一列
					setPkeyByString(rs.getString(1));
				}
			}
			return (BEAN) this;
		} catch (Exception e) {
			throw LOG.err(e, "insert", "新增记录时出错!");
		} finally {
			DbPool.close(stmt, rs);
		}
	}

	public BEAN upd() {
		PreparedStatement stmt = null;
		try {
			chkVersion();
			stmt = DbPool.getInstance().getConn().prepareStatement(((Tb) gtTB()).getSqlUpd());
			int j = toPreparedStatementBean(stmt, 1, false); // 主键字段不更新
			statementSetData(stmt, j, pkeyValues());// Where 之后的主键表达式
			stmt.executeUpdate();
			return (BEAN) this;
		} catch (Exp e) {
			throw e;
		} catch (Exception e) {
			throw LOG.err(e, "update", "更新表【{0}】主键为【{1}】的记录时出错!", gtTB().getName(), pkeysToString(pkeyValues()));
		} finally {
			DbPool.close(stmt);
		}
	}

	public BEAN upd(Fld... flds) {
		PreparedStatement stmt = null;
		try {
			chkVersion();
			stmt = DbPool.getInstance().getConn().prepareStatement(((Tb) gtTB()).getSqlUpd(flds));
			int j = toPreparedStatementBean(stmt, 0, false, flds);
			statementSetData(stmt, j, pkeyValues());// Where 之后的主键表达式
			stmt.executeUpdate();
			return (BEAN) this;
		} catch (Exp e) {
			throw e;
		} catch (Exception e) {
			throw LOG.err(e, "update", "更新表【{0}】主键为【{1}】的记录时出错!", gtTB().getName(), pkeysToString(pkeyValues()));
		} finally {
			DbPool.close(stmt);
		}
	}

	/**
	 * 重读字段，复制主键及不更新的字段，再进行数据更新
	 * 
	 * @param flds
	 * @return
	 */
	public BEAN updReloadWithout(Fld... flds) {
		BEAN bean = (BEAN) loadAndLock(getClass(), pkeyValues());
		PropertyUtils.copyPropertiesWithout(bean, this, flds);
		return upd();
	}

	/**
	 * 重读字段，复制更新的字段，再进行数据更新
	 * 
	 * @param flds
	 * @return
	 */
	public BEAN updReload(Fld... flds) {
		BEAN bean = (BEAN) loadAndLock(getClass(), pkeyValues());
		PropertyUtils.copyProperties(bean, this, flds);
		return upd();
	}

	/**
	 * 更新主键为指定值的fld字段的值在原来的基础上加addValue
	 * 
	 * @param pkey
	 *          主键
	 * @param fld
	 *          字段
	 * @param addValue
	 *          增加的值，要减可用负数
	 * @return
	 */
	public BEAN updAdd(Serializable pkey, Fld fld, long addValue) {
		loadAndLock(pkey);
		long addV = Long.parseLong(propertyValue(fld) + "") + addValue;
		PreparedStatement stmt = null;
		try {
			chkVersion();
			stmt = DbPool.getInstance().getConn().prepareStatement(((Tb) gtTB()).getSqlUpd(fld));
			statementSetData(stmt, 1, addV, pkeyValues());
			stmt.executeUpdate();
			return (BEAN) this;
		} catch (Exp e) {
			throw e;
		} catch (Exception e) {
			throw LOG.err(e, "update", "更新表【{0}】主键为【{1}】的记录时出错!", gtTB().getName(), pkeysToString(pkeyValues()));
		} finally {
			DbPool.close(stmt);
		}

	}

	public void del() {
		PreparedStatement stmt = null;
		int result;
		try {
			chkVersion();
			stmt = DbPool.getInstance().getConn().prepareStatement(((Tb) gtTB()).getSqlDel());
			statementSetData(stmt, 1, pkeyValues());// Where 之后的主键表达式
			result = stmt.executeUpdate();
		} catch (Exp e) {
			throw e;
		} catch (Exception e) {
			throw LOG.err(e, "delete", "删除记录时出错!");
		} finally {
			DbPool.close(stmt);
		}
		if (result == 0)
			throw LOG.err("deleteNotFound", "删除表【{0}】主键为【{1}】的记录不存在!", gtTB().getName(), pkeysToString(pkeyValues()));
	}

	/**
	 * 从数据库中取对象
	 * 
	 * @param pkeys
	 * @return
	 */
	public final BEAN load(Serializable... pkeys) {
		if (chk(pkeys) != null)
			return (BEAN) this;
		throw LOG.err("recordNotFound", "表【{0}】主键条件为【{1}】的记录不存在！", gtTB().getName(), newInstance(gtTB().getClazz())
		    .pkeysToString(pkeys));
	}

	/**
	 * 取数据并加锁，以便修改
	 * 
	 * @param pkeys
	 * @return
	 */
	public final BEAN loadAndLock(Serializable... pkeys) {
		if (chkAndLock(pkeys) != null)
			return (BEAN) this;
		throw LOG.err("recordNotFound", "表【{0}】主键条件为【{1}】的记录不存在！", gtTB().getName(), newInstance(gtTB().getClazz())
		    .pkeysToString(pkeys));
	}

	/**
	 * 检测数据并加锁，以便修改，没找到返回null
	 * 
	 * @param pkeys
	 * @return
	 */
	public final BEAN chk(Serializable... pkeys) {
		return chk(false, pkeys);
	}

	/**
	 * 检测数据并加锁，以便修改，没找到返回null
	 * 
	 * @param pkeys
	 * @return
	 */
	public final BEAN chkAndLock(Serializable... pkeys) {
		return chk(true, pkeys);
	}

	private final BEAN chk(boolean lockRecord, Serializable... pkeys) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if (lockRecord)
				stmt = DbPool.getInstance().getConn().prepareStatement(((Tb) gtTB()).getSqlSelectLock());
			else
				stmt = DbPool.getInstance().getConn().prepareStatement(((Tb) gtTB()).getSqlSelect());
			// stmt.setQueryTimeout(5); // 查询超时5秒
			statementSetData(stmt, 1, pkeys);
			rs = stmt.executeQuery();
			if (!rs.next())
				return null;
			fromResultSet(rs);
			return (BEAN) this;
		} catch (Exception e) {
			throw LOG.err(e, "select", "查询记录时出错!");
		} finally {
			DbPool.close(stmt, rs);
		}
	}

	/**
	 * 锁记录
	 * 
	 * @param model
	 *          模型
	 */
	public BEAN lock() {
		chkAndLock(pkeyValues());
		return (BEAN) this;
	}

	/**
	 * 取字段的值
	 * 
	 * @param fld
	 * @return
	 */
	public final Object propertyValue(Fld fld) {
		try {
			return fld.getGetMethod().invoke(this);
		} catch (Exception e) {
			throw LOG.err(e, "get", "取Bean字段【{0}】的值出错!", fld.getName());
		}
	}

	public final Object propertyValueOBJ(Fld fld) {
		try {
			return fld.getGetMethodOBJ().invoke(this);
		} catch (Exception e) {
			throw LOG.err(e, "get", "取Bean字段【{0}】的值出错!", fld.getName());
		}
	}

	/**
	 * 值字段的值
	 * 
	 * @param fld
	 *          字段
	 * @param val
	 *          值
	 */
	public final void propertySet(Fld fld, Object val) {
		try {
			fld.getSetMethod().invoke(this, val);
		} catch (Exception e) {
			throw LOG.err(e, "set", "对Bean的字段【{0}】赋值出错!", fld.getName());
		}
	}

	/**
	 * 将对象的值置到PreparedStatement对象中
	 * 
	 * @param stmt
	 * @param firstFldId
	 * @param flds
	 *          字段列表，如没有则取表的所有数据库字段
	 * @return
	 */
	public final int toPreparedStatementBean(PreparedStatement stmt, int firstFldId, boolean isIns, Fld... flds) {
		if (flds.length == 0)
			flds = gtTB().getFlds();
		Fld fld;
		try {
			int j = 1;
			for (int i = firstFldId; i < flds.length; i++) {
				fld = flds[i];
				if (!fld.isDatabaseField())
					continue;
				Object obj = propertyValue(fld);
				obj = Env.INST.getDB().toSqlObject(fld, obj);
				stmt.setObject(j, obj);
				j++;
			}
			return j;
		} catch (Exception e) {
			throw LOG.err(e, "setTo", "对象【{0}】赋值到数据库记录时出错!", getClass());
		}
	}

	/**
	 * 从结果集中取数据到Bean对象中，注意结果集中的数据字段顺序必须与字段列表一致！！！
	 * 
	 * @param rs
	 *          结果集
	 * @param flds
	 *          字段列表，没有则取所有字段
	 */
	public final void fromResultSet(ResultSet rs, Fld... flds) {
		if (flds.length == 0)
			flds = gtTB().getFlds();
		int i = 1;
		try {
			for (Fld fld : flds) {
				if (!fld.isDatabaseField())
					continue;
				if (rs.getObject(i) != null) {
					if (fld.getTypeName().equals("CLOB")) {
						Clob clob = rs.getClob(i);
						Reader reader = clob.getCharacterStream();
						char[] c = new char[(int) clob.length()];
						reader.read(c);
						String demo = new String(c);
						reader.close();
						propertySet(fld, demo);
					} else if (fld.getJavaType().equals(Byte.class))
						propertySet(fld, rs.getByte(i)); // 如果为空值，取出的也是0!!1
					else if (fld.getJavaType().equals(Short.class))
						propertySet(fld, rs.getShort(i));
					else if (fld.getJavaType().equals(Integer.class))
						propertySet(fld, rs.getInt(i));
					else if (fld.getJavaType().equals(Long.class))
						propertySet(fld, rs.getLong(i));
					else if (fld.isRaw()) // RAW类型字段
						propertySet(fld, new RAW(rs.getBytes(i)).stringValue());
					else if (rs.getObject(i) instanceof TIMESTAMP)
						propertySet(fld, new Date(((TIMESTAMP) rs.getObject(i)).timestampValue().getTime()));
					else
						propertySet(fld, rs.getObject(i));
				}
				i++;
			}
		} catch (Exception e) {
			throw LOG.err(e, "setFrom", "数据库记录->对象【{0}】时出错!", getClass());
		}
	}

	/**
	 * 取hashCode
	 * 
	 * @return 结果
	 */
	public final int hashCode() {
		Serializable[] keys = pkeyValues();
		Stream.of(keys).forEach(System.out::println);
		if (keys.length == 1)
			return keys[0].hashCode();
		if (keys.length == 2)
			return (keys[0] + ":" + keys[1]).hashCode();
		if (keys.length == 3)
			return (keys[0] + ":" + keys[1] + ":" + keys[3]).hashCode();
		return (keys[0] + ":" + keys[1] + ":" + keys[3] + ":" + keys[4]).hashCode();
	}

	/**
	 * 比较对象
	 * 
	 * @param arg0
	 *          对象
	 * @return 结果
	 */
	@SuppressWarnings("unchecked")
	public int compareTo(Object obj) {
		Serializable[] k2 = ((Bean) obj).pkeyValues();
		Serializable[] k1 = pkeyValues();
		int comp;
		for (int i = 0; i < k1.length; i++) {
			comp = ((Comparable) k1[i]).compareTo(k2[i]);
			if (comp != 0)
				return comp;
		}
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		Serializable[] k2 = ((Bean) obj).pkeyValues();
		Serializable[] k1 = pkeyValues();
		for (int i = 0; i < k1.length; i++) {
			if (!k1[i].equals(k2[i]))
				return false;
		}
		return true;
	}

	/**
	 * 确认可用状态
	 * 
	 * @return 模型
	 */
	public final BEAN assertEnabled() {
		if (propertyValue(gtTB().get("enabled")).toString().equals("1"))
			return (BEAN) this;
		throw LOG.err("{0}状态不是[启用]", toString());
	}

	/**
	 * 确认不可用状态
	 * 
	 * @return
	 */
	public final BEAN assertDisabled() {
		if (propertyValue(gtTB().get("enabled")).toString().equals("0"))
			return (BEAN) this;
		throw LOG.err("{0}状态不是[停用]!", toString());
	}

	/**
	 * 是否为启用状态
	 * 
	 * @param model
	 *          Model
	 * @return 结果
	 */
	@JsonIgnore
	public final boolean isEnabeld() {
		return propertyValue(gtTB().get("enabled")).toString().equals("1");
	}

	public final SysTable gtTable() {
		return SysTable.gtTable(getClass());
	}

	public final void assertImplementsFromITbOutKey() {
		if (ITbOutKey.class.isAssignableFrom(getClass()))
			return;
		throw LOG.err("notImplementsFromITbOutKey", "Bean类【{0}】没有实现接口【{1}】。", getClass().getName(),
		    ITbOutKey.class.getName());
	}

	public final String gtIOutKeyTbAndName() {
		assertImplementsFromITbOutKey();
		ITbOutKey b = (ITbOutKey) this;
		return "[" + gtTable().getShortName() + "]" + b.getName();
	}

	public final String gtIOutKeyTbAndShortName() {
		assertImplementsFromITbOutKey();
		ITbOutKey b = (ITbOutKey) this;
		return "[" + gtTable().getShortName() + "]" + b.getShortName() == null ? b.getName() : b.getShortName();
	}

	/**
	 * 金额四舍五入
	 * 
	 * @param amt
	 *          金额
	 * @return 结果
	 */
	public static final BigDecimal roundAmt(BigDecimal amt) {
		return amt.setScale(AMT_DIGITS, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 数量四舍五入
	 * 
	 * @param amt
	 *          金额
	 * @return 结果
	 */
	public static BigDecimal roundQty(BigDecimal amt) {
		return amt.setScale(QTY_DIGITS, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 数量四舍五入
	 * 
	 * @param amt
	 *          金额
	 * @return 结果
	 */
	public static BigDecimal roundPrice(BigDecimal amt) {
		return amt.setScale(PRICE_DIGITS, BigDecimal.ROUND_HALF_UP);
	}

	public static final Bean gtTbAndPkeyObj(int tbId, int pkey) {
		return get(((SysTable) get(SysTable.class, tbId)).gtClazz(), pkey);
	}

	/**
	 * TODO 根据version字段判断并发数据冲突的问题(待处理)
	 * 每次更改发生version字段值加1
	 */
	private void chkVersion() {
		//		Bean b = Bean.loadAndLock(this.getClass(),pkeyValues());
		//		if(this.getRowVersion()!=b.getRowVersion()) {
		//			throw LOG.err("versionIsChanged", "当前【{0}】的内容已经发生变更！", gtTB().getName());
		//		}
		if (getRowVersion() == null)
			setRowVersion((short) 0);
		this.setRowVersion((short) (this.getRowVersion() + 1));
	}

}
