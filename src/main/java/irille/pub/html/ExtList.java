package irille.pub.html;

import irille.pub.ext.Ext;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.Exts.ExtBase;
import irille.pub.svr.Env;

import java.util.Vector;

/**
 * 列表, 输出格式为： "{ 名称 : 值, 名称 : 值 }"
 * 
 * @author whx
 * 
 */
public class ExtList<THIS extends ExtList> extends ExtBase {
	private Vector<IExtOut> _list = new Vector();
	private ExtList _parent = null;
	private boolean _tabs = true;
	private String[] _closeStr = { "{", "}" };

	/**
	 * 构造方法
	 * @param parent 父对象
	 * @param exts 列表内容
	 */
	public ExtList(ExtList parent, IExtOut... exts) {
		add(exts);
		setParent(parent);
	}

	/**
	 * 构造方法，父对象为null
	 */
	public ExtList() {
		this(null);
	}

	/**
	 * 设定上级对象（列表型的）
	 * @param parent
	 * @return
	 */
	public THIS setParent(ExtList parent) {
		_parent = parent;
		return (THIS) this;
	}

	/**
	 * 设置是否显示前端的空白缩进字符及换行
	 * @param tabs
	 * @return
	 */
	public THIS setTabs(boolean tabs) {
		_tabs = tabs;
		return (THIS) this;
	}

	/**
	 * 返回缩进标志
	 * @return
	 */
	public boolean isTabs() {
		return _tabs;
	}

	/**
	 * 增加列表内容
	 * @param exts 列表内容
	 * @return 本对象
	 */
	public THIS add(IExtOut... exts) {
		for (IExtOut obj : exts)
			_list.add(obj);
		return (THIS) this;
	}

	/**
	 * 增加列表内容
	 * @param ext
	 * @return
	 */
	public THIS add(IExtOut ext) {
		_list.add(ext);
		return (THIS) this;
	}

	/**
	 * 增加属性到列表中
	 * @param name 属性名称
	 * @param value 属性值
	 * @return
	 */
	public THIS add(String name, Object value) {
		_list.add(new ExtAttr(name, value));
		return (THIS) this;
	}

	/**
	 * 增加表达式的属性定义行，输出时没有带“'”
	 * 
	 * @param name
	 *          名称
	 * @param value
	 *          值
	 * @return
	 */
	public THIS addExp(String name, String value) {
		_list.add(new ExtAttr(name).setExp(value));
		return (THIS) this;
	}

	/**
	 * 增加值为列表类型的属性
	 * @param name
	 * @return 返回列表对象，类似此方法，方法名首字为大写的，返回为新建对象的指针！！！
	 */
	public ExtList AddList(String name) {
		ExtAttr ext = new ExtAttr(name);
		_list.add(ext);
		return ext.SetList(this);
	}

	/**
	 * 增加数组对象
	 * @param name 数组名称
	 * @param exps 数组内容项
	 * @return 新增的数组对象
	 */
	public ExtDime AddDime(String name, Object... exps) {
		ExtAttr ext = new ExtAttr(name);
		_list.add(ext);
		return ext.SetDime(exps);
	}

	/**
	 * 新增函数定义
	 * @param name 函数名称
	 * @param paras 函数参数
	 * @return 新增的函数对象
	 */
	public ExtFunDefine AddFunDefine(String name, Object... paras) {
		ExtFunDefine fun = new ExtFunDefine(this, name, paras);
		_list.add(fun);
		return fun;
	}

	/**
	 * 新增函数定义
	 * @param name 函数名称
	 * @return 新增的函数对象
	 */
	public ExtFunDefine AddFunDefine(String name) {
		return AddFunDefine(name, new Object[0]);
	}

	/**
	 * 新增函数调用的代码行
	 * @param name 函数名
	 * @param paras 参数
	 * @return 新增的函数调用对象
	 */
	public ExtFunCall AddFunCall(String name, Object... paras) {
		ExtFunCall fun = new ExtFunCall(this, name, paras);
		_list.add(fun);
		return fun;
	}

	/**
	 * 新增函数调用的代码行
	 * @param name 函数名
	 * @return 新增的函数调用对象
	 */
	public ExtFunCall AddFunCall(String name) {
		return AddFunCall(name, new Object[0]);
	}

	/* (non-Javadoc)
	 * @see irille.pub.html.ExtList#out(int, java.lang.StringBuilder)
	 */
	@Override
	public void out(int tabs, StringBuilder buf) {
		tabs++;
		buf.append(outBefore());
		String s = (isTabs() ? Env.LN + T[tabs] : "");
		String separator = outSeparator() + (isTabs() ? Env.LN + T[tabs] : "");
		for (Object ext : _list) {
			buf.append(s);
			s = separator;
			Ext.outObject(ext, tabs, buf);
		}
		if (isTabs())
			buf.append(LN + T[tabs > 0 ? tabs - 1 : 0] + outAfter());
		else
			buf.append(outAfter() + LN + T[tabs > 0 ? tabs - 1 : 0]);
	}

	/**
	 * 返回父对象节点
	 * @return
	 */
	public ExtList _R() {
		return _parent;
	}

/**
 * 返回类型为指定类的父对象
 * @param clazz 类型
 * @return
 */
	public ExtList _R(Class clazz) {
		return _parent._RR(clazz);
	}

	/**
	 * 返回类型为指定类的父对象
	 * @param clazz 类型
	 * @return
	 */
	private ExtList _RR(Class clazz) {
		if (getClass() == clazz)
			return this;
		return _parent._RR(clazz);
	}

	/**
	 * 返回第i层的父对象
	 * @param i 层级
	 * @return
	 */
	public ExtList _R(int i) {
		return i<=1?_R():_R()._R(i-1);
	}

	/**
	 * 取分隔符，默认为“,”
	 * @return
	 */
	public String outSeparator() {
		return ",";
	}

	/**
	 * 返回输出的前缀
	 * @return
	 */
	public String outBefore() {
		if (_closeStr != null)
			return _closeStr[0];
		return "";
	}

	/**
	 * 返回输出的后缀
	 * @return
	 */
	public String outAfter() {
		if (_closeStr != null)
			return _closeStr[1];
		return "";
	}

	/**
	 * 取父对象
	 * @return
	 */
	public ExtList getParent() {
		return _parent;
	}

	/**
	 * 取输出括起来的字符串数组，下标为[0]的为前缀 ，下标为[1]的为后缀
	 * @return the closeStr
	 */
	public String[] getCloseStr() {
		return _closeStr;
	}

	/**
	 * 设置输出括起来的字符串数组，下标为[0]的为前缀 ，下标为[1]的为后缀
	 * @param closeStr
	 *          the closeStr to set
	 */
	public void setCloseStr(String[] closeStr) {
		_closeStr = closeStr;
	}

	/**
	 * 取存放节点的数组对象 
	 * @return the list
	 */
	public Vector<IExtOut> getNodes() {
		return _list;
	}
}
