package irille.pub.idu;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysTable;
import irille.core.sys.SysUser;
import irille.gl.gs.GsGoods;
import irille.pub.ClassTools;
import irille.pub.Cn;
import irille.pub.Log;
import irille.pub.Objs;
import irille.pub.PropertyUtils;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanForm;
import irille.pub.bean.BeanLong;
import irille.pub.bean.BeanMain;
import irille.pub.bean.BeanStr;
import irille.pub.bean.IGoods;
import irille.pub.bean.IGoodsPrice;
import irille.pub.bean.ISaleLine;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.FldJSONArray;
import irille.pub.tb.FldJSONObject;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.validate.ValidForm;
import irille.pub.validate.ValidNumber;
import irille.pub.validate.ValidRegex;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

/**
 * 数据库功能操基类 在子类中必须声明常量CN，用类似的语句：public static Cn CN = new Cn("功能 代码", "功能名称");
 * @author whx
 * @param <BEAN>
 */
public abstract class Idu<T extends Idu, BEAN extends Bean> {
	private static final Log LOG = new Log(Idu.class);
	public static final Sys.OBillStatus STATUS_INIT = Sys.OBillStatus.INIT;
	public static final Sys.OBillStatus STATUS = Sys.OBillStatus.INIT;

	protected BEAN b;
	protected Short _version; //版本号，前台用户操作时返回的版本
	protected boolean _chkVersion = true; //是否需要判断 版本号
	protected Class _clazz;

	public T setB(BEAN bean) {
		b = bean;
		//考虑到Idu的一种用法Idu.setB()->Idu.commit()然后继续Idu.setB()->Idu.commit();
		//而不是每次都new一个Idu实例，所以每次设置setB后都把version清空；
		_version = null;
		return (T) this;
	}

	public T setBKey(Serializable... pkeys) {
		b = load(pkeys);
		return (T) this;
	}

	public Short getVersion() {
		return _version;
	}

	public void setVersion(Short version) {
		_version = version;
	}
	
	public boolean getChkVersion() {
		return _chkVersion;
	}

	public void setChkVersion(boolean chkVersion) {
		_chkVersion = chkVersion;
	}
	public void checkVersion() {
		if(!_chkVersion) return ;
		if(b==null) return; //list的判断
		if(b.getPkey()==null) return; //ins的判断
		if(_version==null) {//upd ，del
			_version = b.getRowVersion();
			//Bean bean = BeanBase.loadAndLock(b.clazz(), b.getPkey());
			Bean bean = BeanBase.chk(b.clazz(), b.getPkey()); 
			if(bean==null) return;//数据库中没有记录，说明是新增；
			if(!_version.equals(bean.getRowVersion())) throw LOG.err("versionIsChanged", "当前内容已经发生变更！");
		} else {//form中的操作，如Approve，tally
			if(!_version.equals(b.getRowVersion())) throw LOG.err("versionIsChanged", "当前内容已经发生变更！");
		}
	}
	/**
	 * 判断本记录可否进行当前操作
	 * 
	 * @return
	 */
	public boolean isToDo() {
		return true;
	}

	/**
	 * 分别调用before,valid,run,after方法
	 */
	public abstract void commit();

	/**
	 * 前处理
	 */
	public void before() {
		checkVersion();
	}

	/**
	 * 校验--由所有功能子类调用
	 */
	public void valid() {
	}

	public void assertStatusIsInit(BeanForm bean) {
		assertStatus(bean, STATUS_INIT);
	}

	public void assertStatusIsCheck(BeanForm bean) {
		assertStatus(bean, STATUS.CHECKED);
	}

	public void assertStatusIsTally(BeanForm bean) {
		assertStatus(bean, STATUS.TALLY_ABLE);
	}

	public void assertStatusIsDone(BeanForm bean) {
		assertStatus(bean, STATUS.DONE);
	}

	public void assertStatus(BeanForm form, Sys.OBillStatus... statuses) {
		for (Sys.OBillStatus s : statuses) {
			if (form.getStatus() == s.getLine().getKey())
				return;
		}
		Cn cn = (Cn) ClassTools.getStaticProerty(getClass(), "CN");
		throw LOG.err("assertStatus", form.gtTB().getName() + "[" + form.getCode() + "]的状态为["
		    + form.gtStatus().getLine().getName() + "]，不可" + cn.getName()+ "！");
	}

	public static void assertStatusOther(BeanForm form, Sys.OBillStatus... statuses) {
		for (Sys.OBillStatus s : statuses) {
			if (form.getStatus() == s.getLine().getKey())
				return;
		}
		throw LOG.err("assertStatus", form.gtTB().getName() + "[" + form.getCode() + "]的状态为["
		    + form.gtStatus().getLine().getName() + "]，不可操作！");
	}

	/**
	 * 校验--仅由新增与修改的功能子类调用
	 */
	public void validIu() {
		Fld[] flds = getTB().getFlds();
		for (Fld fld : flds) {
			if (fld instanceof FldLines || fld instanceof FldV)
				continue;
			if (fld.isNull() || fld.getCode().equals("pkey"))
				continue;
			Object obj = ClassTools.getProperty(getB(), fld.getCode());
			if (obj == null || Str.isEmpty(obj.toString()))
				throw LOG.err("validate", fld.getName() + "不可为空，请输入");
			if (fld.getJavaType() == String.class && !(fld instanceof FldJSONObject) && !(fld instanceof FldJSONArray) && obj.toString().length() > fld.getLen())
				throw LOG.err("validate", fld.getName() + "内容长度不可超过[" + fld.getLen() + "]");
		}
	}

	public static void validFromOut(Bean bean, Fld... iflds) {
		Fld[] flds = bean.gtTB().getFlds();
		for (Fld fld : flds) {
			if (fld instanceof FldLines || fld instanceof FldV)
				continue;
			if (fld.isNull() || fld.getCode().equals("pkey"))
				continue;
			if (iflds != null && Objs.in(iflds, fld))
				continue;
			Object obj = ClassTools.getProperty(bean, fld.getCode());

			if (obj == null || Str.isEmpty(obj.toString()))
				throw LOG.err("validate", fld.getName() + "不可为空，请输入");
			if (fld.getJavaType() == String.class && !(fld instanceof FldJSONObject) && !(fld instanceof FldJSONArray) && obj.toString().length() > fld.getLen())
				throw LOG.err("validate", fld.getName() + "内容长度不可超过[" + fld.getLen() + "]");
		}
	}

	/**
	 * 执行
	 */
	public void run() {
	}

	/**
	 * 后处理
	 */
	public void after() {
	}

	public BEAN getB() {
		return b;
	}

	/**
	 * 记系统日志 TODO
	 */
	public void log() {
		// if (b == null) {
		// if (SysLogopDAO.IGNORE.contains(clazz().getSimpleName()) == false)
		// SysLogopDAO.log(getCn().getName(), Sys.OLogOptype.FUN,
		// getCn().getName());
		// return;
		// }
		// if (SysLogopDAO.IGNORE.contains(b.clazz().getSimpleName()) == false)
		// SysLogopDAO.logOp(getCn().getName(), b);
		if (b != null) {
			Integer id = Tb.chkID(b.clazz());
			if (id != null && b instanceof BeanStr == false)
				Env.getTran().setObj(id, Integer.parseInt(b.pkeyValues()[0] + ""));
		}
	}

	/**
	 * 取Cn, 必须在类中定义静态的常量CN，以便被此方法取用
	 * 
	 * @return
	 */
	public final Cn getCn() {
		return (Cn) ClassTools.getStaticProerty(getClass(), "CN");
	}

	public final String getCode() {
		return getCn().getCode();
	}

	public final String getName() {
		return getCn().getName();
	}

	/**
	 * 获取功能类中，泛型Bean的实际类型
	 * 
	 * @return
	 */
	public final Class<BEAN> clazz() {
		if (_clazz != null)
			return _clazz;
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] ts = ((ParameterizedType) type).getActualTypeArguments();
			if (ts.length >= 2 && ts[1] instanceof Class)
				_clazz = (Class) ts[1];
		}
		if (_clazz == null)
			throw LOG.err("iduClazz", "{0}取Bean的类出错", this);
		return _clazz;
	}

	public void setClazz(Class clazz) {
		_clazz = clazz;
	}

	/**
	 * 取TB对象
	 */
	public final Tb getTB() {
		return Tb.getTBByBean(clazz());
	}

	/**
	 * 新建POJO对象
	 * 
	 * @return
	 */
	public BEAN newBean() {
		return (BEAN) ClassTools.newInstance(clazz());
	}

	/**
	 * 从数据库中取对象
	 * 
	 * @param pkeys
	 * @return
	 */
	public final BEAN load(Serializable... pkeys) {
		return (BEAN) newBean().load(pkeys);
	}

	/**
	 * 取数据并加锁，以便修改
	 * @param pkeys
	 * @return
	 */
	public final BEAN loadAndLock(Serializable... pkeys) {
		return (BEAN) newBean().loadAndLock(pkeys);
	}

	/**
	 * 取数据并加锁，以便修改
	 * @param pkeys
	 * @return
	 */
	public final BEAN loadThisBeanAndLock() {
		return (BEAN) newBean().loadAndLock(getB().pkeyValues());
	}

	/**
	 * 检测数据，没找到返回null
	 * @param pkeys
	 * @return
	 */
	public final BEAN chk(Serializable... pkeys) {
		return (BEAN) newBean().chk(pkeys);
	}

	/**
	 * 检测数据并加锁，以便修改，没找到返回null
	 * 
	 * @param pkeys
	 * @return
	 */
	public final BEAN chkAndLock(Serializable... pkeys) {
		return (BEAN) newBean().chk(true, pkeys);
	}

	/**
	 * 分页查询
	 * @param idx 起始位置
	 * @param count 分页数量
	 * @return
	 */
	public List<BEAN> list(int idx, int count) {
		return BeanBase.list(clazz(), false, "", idx, count);
	}

	/**
	 * 分页查询
	 * @param idx 起始位置
	 * @param count 分页数量
	 * @param where 条件语句(不用带where)
	 * @param vals 条件参数
	 * @return
	 */
	public List<BEAN> list(int idx, int count, String where, Serializable... vals) {
		return BeanBase.list(clazz(), false, where, idx, count, vals);
	}

	/**
	 * 取所有记录集合
	 * @return
	 */
	public final List<BEAN> getAll() {
		return BeanBase.list(clazz(), "", false);
	}

	/**
	 * 取所以记录条数
	 * @return
	 */
	public final int count() {
		return countWhere(null);
	}

	/**
	 * 过滤查询记录条数
	 * @param whereSql 查询条件
	 * @param paras 参数
	 * @return
	 */
	public int countWhere(String whereSql, Serializable... paras) {
		if (whereSql == null || whereSql.trim().isEmpty())
			whereSql = "1=1";
		String sql = "SELECT count(*) FROM " + getTB().getCodeSqlTb() + " WHERE " + whereSql;
		return ((Number) BeanBase.queryOneRow(sql, paras)[0]).intValue();
	}

	/**
	 * 取用户对象
	 * @return 用户
	 */
	public final static SysUser getUser() {
		return Env.INST.getTran().getUser();
	}

	/**
	 * 取机构
	 * @return 结果
	 */
	public final static SysOrg getOrg() {
		return getUser().gtOrg();
	}

	public final static SysCell getCell() {
		SysDept dept = getDept();
		if (dept.getCell() != null)
			return dept.gtCell();
		return SysCellDAO.getCellByOrg(dept.getOrg());
	}

	/**
	 * 取部门
	 * @return 结果
	 */
	public static SysDept getDept() {
		return getUser().gtDept();
	}

	/**
	 * 根据FLD、CLASS对象产生SQL语句
	 * @param sql SQL语句参数为{0}格式
	 * @param paras 参数
	 * @return
	 */
	public static final String sqlString(String sql, Object... paras) {
		Object obj;
		for (int i = 0; i < paras.length; i++) {
			obj = paras[i];
			if (obj instanceof Fld)
				paras[i] = ((Fld) obj).getCodeSqlField();
			else if (obj instanceof Class)
				paras[i] = Tb.getTBByBean(((Class) obj)).getCodeSqlTb();
			else if (obj instanceof IEnumFld)
				paras[i] = ((IEnumFld) obj).getFld().getCodeSqlField();
			else if (obj instanceof IEnumOpt)
				paras[i] = ((IEnumOpt) obj).getLine().getKey();
		}
		return MessageFormat.format(sql, paras);
	}

	public static final void insLine(BeanMain main, List list, Fld field) {
		for (Bean bean : (List<Bean>) list) {
			bean.propertySet(field, main.getPkey());
			bean.setRowVersion((short) 0);
			validFromOut(bean);
			bean.ins();
		}
	}

	/**
	 * 明细主键的规则 = 主表主键 -> 清空后6位为0 + 序号
	 * @param main
	 * @param list
	 * @param field
	 */
	public static final void insLineTid(BeanLong main, List list) {
		long pkey = lineFirstPkey(main.getPkey());
		int num = 1;
		for (BeanLong bean : (List<BeanLong>) list) {
			bean.setPkey(pkey + num);
			bean.setRowVersion((short) 0);
			validFromOut(bean);
			bean.ins();
			num++;
		}
	}

	public static final long lineFirstPkey(long mainPkey) {
		return mainPkey * SysTable.NUM_BASE;
	}

	public static final void delLine(List set) {
		for (Bean bean : (List<Bean>) set) {
			bean.del();
		}
	}

	public static final void delLineTid(BeanLong bean, Class clazz) {
		long mainpkey = lineFirstPkey(bean.getPkey());
		long mainpkey2 = mainpkey + SysTable.NUM_BASE;
		String sql = Idu.sqlString("delete from {0} where pkey>? and pkey <?", clazz);
		BeanBase.executeUpdate(sql, mainpkey, mainpkey2);
	}

	public static final void updLine(BeanMain main, List list, Fld field) {
		String wheresql = field.getCodeSqlField() + "=?";
		List<Bean> listOld = BeanBase.list(field.getTb().getClazz(), wheresql, false, main.pkeyValues()); // 数据库旧数据
		boolean insFlag;
		for (Bean formBean : (List<Bean>) list) {
			formBean.propertySet(field, main.getPkey());
			insFlag = true; // 默认新增标志
			for (Bean bean : listOld) {
				if (bean.equals(formBean)) {
					insFlag = false; // 修改标志
					PropertyUtils.copyWithoutCollection(bean, formBean);
					validFromOut(bean);
					bean.upd();
					break;
				}
			}
			if (insFlag) {
				formBean.setRowVersion((short) 0);
				validFromOut(formBean);
				formBean.ins();
			}
		}
		// 删除不存的数据
		for (Bean bean : listOld) {
			if (list.contains(bean))
				continue;
			bean.del();
		}
	}

	public static final void updLineTid(BeanLong main, List list, Class lineClazz) {
		long mainpkey = lineFirstPkey(main.getPkey());
		long mainpkey2 = mainpkey + SysTable.NUM_BASE;
		int num = 0;
		String wheresql = "pkey > " + mainpkey + " and pkey < " + mainpkey2;
		List<Bean> listOld = BeanBase.list(lineClazz, wheresql, true); // 数据库旧数据
		for (BeanLong formBean : (List<BeanLong>) list) {
			if (formBean.getPkey() == null)
				continue;
			for (Bean bean : listOld) {
				if (bean.equals(formBean)) {
					PropertyUtils.copyWithoutCollection(bean, formBean);
					bean.upd();
					if (formBean.getPkey() % SysTable.NUM_BASE > num)
						num = (int) (formBean.getPkey() % SysTable.NUM_BASE);
					break;
				}
			}
		}
		// 删除不存的数据
		for (Bean bean : listOld) {
			if (list.contains(bean))
				continue;
			bean.del();
		}
		// 新增
		for (BeanLong formBean : (List<BeanLong>) list) {
			if (formBean.getPkey() != null)
				continue;
			num++;
			formBean.setPkey(mainpkey + num);
			formBean.setRowVersion((short) 0);
			validFromOut(formBean);
			formBean.ins();
		}
	}

	/**
	 * 
	 * @param bean
	 *          主表对象
	 * @param clazz
	 *          明细表CLASS
	 * @return
	 */
	public static List getLinesTid(BeanLong bean, Class clazz, int idx, int count) {
		long mainpkey = lineFirstPkey(bean.getPkey());
		long mainpkey2 = mainpkey + SysTable.NUM_BASE;
		return BeanBase.list(clazz, false, "pkey>? and pkey <?", idx, count, mainpkey, mainpkey2);
	}

	public static List getLinesTid(BeanLong bean, Class clazz) {
		return getLinesTid(bean, clazz, 0, 0);
	}

	public static int getLinesTidCount(BeanLong bean, Class clazz) {
		long mainpkey = lineFirstPkey(bean.getPkey());
		long mainpkey2 = mainpkey + SysTable.NUM_BASE;
		String sql = Idu.sqlString("SELECT count(*) FROM {0} WHERE pkey>? and pkey<?", clazz);
		return ((Number) BeanBase.queryOneRow(sql, mainpkey, mainpkey2)[0]).intValue();
	}

	public static List getLines(IEnumFld fld, Serializable pkey) {
		return getLines(fld.getFld(), pkey);
	}

	public static List getLines(Fld fld, Serializable pkey) {
		return BeanBase.list(fld.getTb().getClazz(), fld.getCodeSqlField() + "=? ORDER BY pkey", false, pkey);
	}

	public static int getLinesCount(Fld fld, Serializable pkey) {
		return ((Number) BeanBase.queryOneRow(
		    "SELECT count(*) FROM " + ((Tb) fld.getTb()).getCodeSqlTb() + " WHERE " + fld.getCodeSqlField()
		        + "=? ORDER BY pkey", pkey)[0]).intValue();
	}

	/**
	 * 重新计算金额
	 * 检查单据信息中货物相关的合法性
	 * @param list
	 * @return 返回合计金额
	 */
	public static BigDecimal sumAmt(List list) {
		if (list == null || list.size() == 0)
			return BigDecimal.ZERO;
		BigDecimal sum = BigDecimal.ZERO;
		for (IGoodsPrice line : (List<IGoodsPrice>) list)
			sum = sum.add(line.getAmt());
		return sum;
	}

	public static final <T extends IGoods> T checkGoodsIn(List<T> erpGoodsUomQtyInfLines, GsGoods goods) {
		for (T m : erpGoodsUomQtyInfLines)
			if (m.getGoods().equals(goods.getPkey()))
				return m;
		return null;
	}

	public static void checkFormGoods(List list) {
		if (list == null || list.size() == 0)
			throw LOG.err("noLines", "单据货物信息不可为空");
		for (IGoods line : (List<IGoods>) list) {
			if (line == null || line.getGoods() == null || line.getQty() == null || line.getUom() == null)
				throw LOG.err("errLine", "请完善单据货物明细信息!");
			if (line.getQty().signum() < 1)
				throw LOG.err("errLine", "货物数量必须大于零!");
			if (line.gtGoods().gtUom().getUomType().intValue() != line.gtUom().getUomType().intValue())
				throw LOG.err("errLine", "计量单位[{0}]与货物[{1}]的默认单位类型不一致!", line.gtUom().getName(), line.gtGoods().getName());
		}
	}

	public static void checkFormGoodsPrice(List list) {
		if (list == null || list.size() == 0)
			throw LOG.err("noLines", "单据货物信息不可为空");
		for (IGoodsPrice line : (List<IGoodsPrice>) list) {
			if (line == null || line.getGoods() == null || line.getQty() == null || line.getPrice() == null
			    || line.getUom() == null)
				throw LOG.err("errLine", "请完善单据货物明细信息!");
			if (line.getPrice().signum() == -1)
				throw LOG.err("errLine", "货物单价不可小于零!");
			if (line.getQty().signum() == 0)
				throw LOG.err("errLine", "货物数量不可小于或等于零!");
			if (line.gtGoods().gtUom().getUomType().intValue() != line.gtUom().getUomType().intValue())
				throw LOG.err("errLine", "计量单位[{0}]与货物[{1}]的默认单位类型不一致!", line.gtUom().getName(), line.gtGoods().getName());
//			line.setAmt(line.getQty().multiply(line.getPrice()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP)); by whx 重新计算后需要将结果四舍五入；
			if (line instanceof ISaleLine)
				((ISaleLine) line).setExpensesSale(BigDecimal.ZERO);
		}
	}
	
	//whx 2015/5/18
	public static <T extends Bean> void haveBeUsed(Class<T> beanClass, Object param, Serializable val) {
		String where = sqlString("{0}=?", param);
		if(BeanBase.list(beanClass, where, false	, val).size()!=0) {
			throw LOG.err("delerror", "当前记录存在对应的[{0}]信息，不能删除！", Tb.getTBByBean(beanClass).getName());
		}
	}
	public static <T extends Bean> boolean chkBeUsed(Class<T> beanClass, Object param, Serializable val) {
		String where = sqlString("{0}=?", param);
		if(BeanBase.list(beanClass, where, false	, val).size()!=0) 
			return true;
		return false;
	}
	
	/**
	 * 获取表单校验器 
	 * @return
	 */
	public ValidForm FormValid() {
		return new ValidForm(this);
	}
	public ValidForm FormValid(Bean bean) {
		return new ValidForm(bean);
	}
	/**
	 * 获取正则校验器
	 * @return
	 */
	public ValidRegex RegexValid() {
		return new ValidRegex(this);
	}
	public ValidRegex RegexValid(Bean bean) {
		return new ValidRegex(bean);
	}
	/**
	 * 获取数字校验器
	 */
	public ValidNumber numberValid() {
		return new ValidNumber(this);
	}
	public ValidNumber numberValid(Bean bean) {
		return new ValidNumber(bean);
	}
}
