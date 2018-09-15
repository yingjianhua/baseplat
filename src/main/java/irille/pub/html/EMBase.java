/**
 * 
 */
package irille.pub.html;

import irille.core.sys.SysMenu;
import irille.core.sys.SysTable;
import irille.pub.Exp;
import irille.pub.FileTools;
import irille.pub.bean.BeanBase;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.Exts.ExtAct;
import irille.pub.html.Exts.ExtActs;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * Ext2 模块文件的基类,用于产生各种类型的单个JS文件
 * @author whx
 *
 */
public abstract class EMBase<T extends EMBase> extends ExtFile<T> {
	private Tb _tb;
	private ExtFunDefine _fun = new ExtFunDefine(null, "initComponent", new Object[0]); //初始化组件的方法对象
	private ExtActs _acts = new ExtActs(); //功能清单的容器
	private ExtList _form = new ExtList(); //主屏对象
	private ExtDime _columns = new ExtDime(); //主屏的操作字段对象
	private VFlds _vflds; //可以直接操作，主屏的VFlds对象
	private String _pack, _clazz;
	private Vector<ExtFunDefine> _otherFuns = new Vector(); //其它函数定义的数组
	//直接导入JS代码段的要替换的参数信息（因为Tb中定义的Act是自动导入的，所以在此指定参数）
	private Hashtable<String, Object[]> _loadFunCodeFmtParas = new Hashtable();
	//直接导入的函数的参数（因为Tb中定义的Act是自动导入的，所以在此指定参数）
	private Hashtable<String, Object[]> _loadFunParas = new Hashtable();

	private int _indexGoods = 0; //货物出现在明细表中的下标

	/**
	 * 构造方法
	 * @param tb Tb对象
	 * @param vflds 主区域的字段列表
	 */
	public EMBase(Tb tb, VFlds... vflds) {
		_tb = tb;
		_vflds = VFlds.newVFlds(vflds);
		_pack = getTbPackage(getTb());
		_clazz = getTbClazz(getTb());
	}

	/**
	 * 构造方法
	 * @param tb
	 */
	public EMBase(Tb tb) {
		this(tb, null);
	}

	/**
	 * 初始化，顺序为：
	 * initForm(); //初始化Form对象
	 * initActs(); //初始化功能
	 * initColumns(); //初始化字段
	 * initComponent(_fun); //初始化组件
	 * initFuns(); //初始化其他函数
	 */
	public T init() {
		super.init();
		initForm(); //初始化Form对象
		initActs(); //初始化功能
		initColumns(); //初始化字段

		add(_fun); //增加函数
		initComponent(_fun); //初始化组件

		initFuns(); //初始化其他函数
		return (T) this;
	}

	/**
	 * 初始化主屏的显示字段信息
	 */
	public void initColumns() {
	}

	/**
	 * 初始化Act功能
	 */
	public void initActs() {
	}

	/**
	 * 初始化JS的主屏幕对象
	 */
	public void initForm() {
	}

	/**
	 * 初始化组件
	 * @param fun
	 */
	public void initComponent(ExtFunDefine fun) {
	}

	/**
	 * 取直接嵌入的JS代码所在的文件，因为有继承类时，一些自动代码可能无法读取，所以要在此方法中进行判断，一般情况下直接指定类名。
	 * @param act 功能
	 * @return
	 */
	public Class getProgramCodeFile(Act act) {
		return getClass();
	}

	/**
	 * 初始化功能函数
	 */
	public void initFuns() {
		initFunsAddOtherFuns();
		initFunsAddActs();
	}

	public int getIndexGoods() {
		return _indexGoods;
	}

	public void setIndexGoods(int indexGoods) {
		_indexGoods = indexGoods;
	}

	/**
	 * 将自定义的功能加到JS的对象树中
	 */
	public void initFunsAddOtherFuns() {
		for (ExtFunDefine fun : _otherFuns)
			add(fun);
	}

	/**
	 * 将所有的功能加到JS的对象树中
	 */
	public void initFunsAddActs() {
		for (ExtAct act : getActs().getActs()) {
			initFunsAddAct(act);
		}
	}

	/**
	 * 将指定的功能加到JS对象树中
	 * @param act
	 */
	public void initFunsAddAct(ExtAct act) {
		add(act.getFun());
	}

	/**
	 * 从JAVA代码文件中读取JS的代码，并将参数进行替换，JS代码段采用如下形式： "** Begin "+name+" **" JS代码行【0】 【1】
	 * "** End "+name+" **" 其中“【0】”为替换的参数
	 * 
	 * @param clazz 类，取该类源代码中的指定代码行
	 * @param name 名称
	 * @param fmtParas 需要替换的参数
	 * @return
	 */
	public String loadFunCode(Class clazz, String name, Object... fmtParas) {
		String lns = null;
		try {
			lns = FileTools.loadJavaFileLines(Class.forName("irille.dep." + this._pack + ".E" + this._clazz), "** Begin "
			    + name + " **", "** End " + name + " **");
		} catch (Exception e) {
			try {
				lns = FileTools.loadJavaFileLines(clazz, "** Begin " + name + " **", "** End " + name + " **");
			} catch (Exp e1) {
				throw e1;
			}
		}
		// System.out.println(lns);
		int i = 0;
		for (Object obj : getLoadFunCodeFmtParas(clazz, name, fmtParas)) {
			lns = lns.replaceAll("【" + (i++) + "】", obj.toString());
		}
		return lns;
	}

	/**
	 * 取JS代码段的参数（如果提前设定的话）
	 * @param clazz 类名
	 * @param name 名称（一般为函数名）
	 * @param fmtParas 要替换的参数，如参数没有，则从loadFunCodeFmtParas中指是否有提前指定的参数，有则返回。
	 * @return
	 */
	public Object[] getLoadFunCodeFmtParas(Class clazz, String name, Object... fmtParas) {
		if (fmtParas.length != 0)
			return fmtParas;
		Object[] ss = _loadFunCodeFmtParas.get(clazz.getClass() + name);
		if (ss == null)
			return new Object[] { getPack(), getClazz() };
		return ss;
	}

	/**
	 * 置JS代码段的参数
	 * @param clazz 源代码所在类
	 * @param name 名称
	 * @param fmtParas 参数
	 * @return
	 */
	public T setLoadFunCodeFmtParas(Class clazz, String name, Object... fmtParas) {
		_loadFunCodeFmtParas.put(clazz.getClass() + name, fmtParas);
		return (T) this;
	}

	/**
	 * 置JS代码段的参数
	 * @param clazz 源代码所在类
	 * @param act 功能
	 * @param fmtParas 参数
	 * @return
	 */
	public T setLoadFunCodeFmtParas(Class clazz, IEnumOpt act, Object... fmtParas) {
		return setLoadFunCodeFmtParas(clazz, Act.funName(act), fmtParas);
	}

	/**
	 * 取指定函数的参数（如果提前设定的话）
	 * @param clazz 类
	 * @param name 名称
	 * @param funParas 参数
	 * @return
	 */
	public Object[] getLoadFunParas(Class clazz, String name, Object... funParas) {
		if (funParas.length != 0)
			return funParas;
		Object[] ss = _loadFunParas.get(clazz.getClass() + name);
		if (ss == null)
			return new Object[] {};
		return ss;
	}

	/**
	 * 置JS定义函数的参数，一般用于TB对象中的Act方法，因为会自动产生
	 * @param clazz 类
	 * @param name 名称
	 * @param paras 参数
	 * @return
	 */
	public T setLoadFunParas(Class clazz, String name, Object... paras) {
		_loadFunParas.put(clazz.getClass() + name, paras);
		return (T) this;
	}

	/**
	 * 置JS定义函数的参数，一般用于TB对象中的Act方法，因为会自动产生
	 * @param clazz 类
	 * @param act 功能
	 * @param paras 参数
	 * @return
	 */
	public T setLoadFunParas(Class clazz, IEnumOpt act, Object... paras) {
		return setLoadFunParas(clazz, Act.funName(act), paras);
	}

	/**
	 * 将Tb对象的Act导入
	 * @param funCodeFile
	 */
	public void loadTbActs(Class funCodeFile) {
		for (Act act : (Vector<Act>) getTb().getActs()) {
			if (act.getAct() == OAct.LIST)
				continue;
			loadTbAct(funCodeFile, act);
		}
	}

	/**
	 * 将Tb的Act加到对象中
	 * @param funCodeFile 类文件
	 * @param act 功能
	 */
	public void loadTbAct(Class funCodeFile, Act act) {
		AddAct(act, funCodeFile);
	}

	/**
	 * 增加功能
	 * @param name 名称
	 * @param funCodeFile 类文件
	 * @param loadFunCodeFmtParas 要替换的参数
	 * @return
	 */
	public ExtFunDefine AddFun(String name, Class funCodeFile, Object... loadFunCodeFmtParas) {
		ExtFunDefine fun = new ExtFunDefine(this, name);
		setLoadFunCodeFmtParas(funCodeFile, name, loadFunCodeFmtParas);
		fun.add(loadFunCode(funCodeFile, name));
		_otherFuns.add(fun);
		return fun;
	}

	/**
	 * 增加功能
	 * @param name 名称
	 * @param funCodeFile 类文件
	 * @param paras 参数
	 * @return
	 */
	public ExtFunDefine AddFunD(String name, Class funCodeFile, Object... paras) {
		ExtFunDefine fun = new ExtFunDefine(this, name, paras);
		fun.add(loadFunCode(funCodeFile, name));
		_otherFuns.add(fun);
		return fun;
	}

	/**
	 * 取JS的主Form对象
	 * @return
	 */
	public ExtList getForm() {
		return _form;
	}

	/**
	 * 取功能集对象
	 * @return
	 */
	public ExtActs getActs() {
		return _acts;
	}

	/**
	 * 取功能
	 * @param act 功能
	 * @return
	 */
	public ExtAct getAct(IEnumOpt act) {
		return _acts.getAct(act);
	}

	public final VFlds getVFlds() {
		return _vflds;
	}
	
	public void setVflds(VFlds vflds) {
		_vflds = vflds;
	}

	/**
	 * 增加功能
	 * @param act 功能
	 * @return
	 */
	public ExtAct AddAct(Act act) {
		return AddAct(act, getProgramCodeFile(act)); //
	}

	/**
	 * 增加功能
	 * @param act 功能
	 * @param funCodeFile 类文件
	 * @param funCodeParas 参数
	 * @return
	 */
	public ExtAct AddAct(Act act, Class funCodeFile, Object... funCodeParas) {
		return getActs().Add(this, act, funCodeFile, funCodeParas); //
	}

	/**
	 * 增加窗口中的功能，本方法用于兼容以前的JS产生器，一些参数顺序与AddActRow的不同，实质是一样的！
	 * @param act 功能
	 * @return
	 */
	public ExtAct AddActWin(Act act) {
		return AddActWin(act, getProgramCodeFile(act)); //
	}

	/**
	 * 增加窗口中的功能，本方法用于兼容以前的JS产生器，一些参数顺序与AddActRow的不同，实质是一样的！
	 * @param act 功能
	 * @param funCodeFile 类代码
	 * @param funCodeParas 参数
	 * @return
	 */
	public ExtAct AddActWin(Act act, Class funCodeFile, Object... funCodeParas) {
		return getActs().AddActWin(this, act, funCodeFile, funCodeParas); //
	}

	/**
	 * 增加功能，与AddActWin的内容一样，只不过参数的顺序有所不同，用于兼容以前的代码产生器
	 * @param act 功能
	 * @param funCodeFile 类代码
	 * @param funCodeParas 参数
	 * @return
	 */
	public ExtAct AddActRow(Act act, Class funCodeFile, Object... funCodeParas) {
		return getActs().AddActRow(this, act, funCodeFile, funCodeParas); //
	}

	/**
	 * 取Fun的节点
	 * @return
	 */
	public final ExtFunDefine getFun() {
		return _fun;
	}

	/**
	 * 取指定Vfld
	 * @param fld IEnumFld对象
	 * @return 结果
	 */
	public final VFld getVfld(IEnumFld fld) {
		return _vflds.get(fld);
	}

	/**
	 * @return 取包名的字符串
	 */
	public final String getPack() {
		return _pack;
	}

	/**
	 * 将VFld对象的扩展属性(attrs)追加到fldList对象中
	 * @param fld VFld对象
	 * @param fldList fldList节点
	 */
	public void setFldAttr(VFld fld, ExtList fldList) {
		if (fld.attrs() == null)
			return;
		for (IExtOut ext : (Vector<IExtOut>) fld.attrs().getNodes())
			fldList.add(ext);
	}

	/**
	 * @return the clazz
	 */
	public final String getClazz() {
		return _clazz;
	}

	/**
	 * 取Tb对象
	 * @return
	 */
	public final Tb getTb() {
		return _tb;
	}

	/**
	 * 取其它函数节点
	 * @return the otherFuns
	 */
	public Vector<ExtFunDefine> getOtherFuns() {
		return _otherFuns;
	}

	/**
	 * 取Columns节点
	 * @return the columns
	 */
	public ExtDime getColumns() {
		return _columns;
	}

	/**
	 * 将明细Act代码的后3个字符"Row"去掉
	 * @param act
	 * @return
	 */
	public static String actCodeCutRow(Act act) {
		String s = act.getCode();
		return s.substring(0, s.length() - 3);
	}

	public static void initOutFldJump(VFld vfld, ExtList extList) {
		if (vfld.getFld() instanceof FldOutKey) {
			Class outClass = ((FldOutKey) vfld.getFld()).getOutTbClazz();
			if (SysTable.class.equals(outClass))
				return;
			Tb tb = BeanBase.tb(outClass);
			String value = getMd(tb);
			if (value != null)
				vfld.attrs().add("md", value);
			value = getMn(tb);
			if (value != null)
				vfld.attrs().add("mn", value);
			if (value != null) { //更改渲染器
				Vector<IExtOut> outs = extList.getNodes();
				for (IExtOut out : outs) {
					if (out instanceof ExtAttr == false)
						continue;
					ExtAttr attr = (ExtAttr) out;
					if (attr.getName().equals(RENDERER) == false)
						continue;
					attr.setExp("mvc.Tools.beanRendererHref()");
					break;
				}
			}
		}
	}

	public static String getMd(Tb tb) {
		List<Object[]> list = BeanBase.list("select DISTINCT(menu) from sys_menu_act where table_code = ?", tb.getClazz()
		    .getName());
		if (list.size() == 0)
			return null;
		SysMenu menu = BeanBase.load(SysMenu.class, Integer.parseInt(list.get(0)[0].toString()));
		return menu.getType();
	}

	public static String getMn(Tb tb) {
		List<Object[]> list = BeanBase.list("select DISTINCT(menu) from sys_menu_act where table_code = ?", tb.getClazz()
		    .getName());
		if (list.size() == 0)
			return null;
		SysMenu menu = BeanBase.load(SysMenu.class, Integer.parseInt(list.get(0)[0].toString()));
		return menu.getUrl();
	}

}
