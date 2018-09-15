package irille.pub.html;

import irille.pub.Log;

import java.util.Vector;


/**
 * Css文件中的行，每一行有几个标签（包括派生选择器）及若干个属性组成
 * 
 * @author whx
 * 
 */
public class CssLine extends Style<CssLine> {
	private static final Log LOG = new Log(CssLine.class);
	public static final String ELELEMENT = "";
	public static final String CLASS = ".";
	public static final String ID = "#";
	public static final String REM = "*";
	private Css _css = null;
	private String _name;
	private String _parent = ""; // 父标签
	private String _children = ""; // 子标签
	private Vector<CssLine> _links = null;
	private String _type;

	/**
	 * name可以包含派生选择器，如："li strong",但getName()返回的是“li", getChildren()返回的是"strong"
	 * 
	 * @param css
	 *          属于哪个CSS文件
	 * @param name
	 *          标签名
	 * @param as
	 *          属性列表
	 */
	public CssLine(Css css, String name, AttrBase... as) {
		super(as);
		_css = css;
		char c = name.toCharArray()[0];
		if (c == '.') {
			_type = CLASS;
		} else if (c == '#') {
			_type = ID;
		} else if (c == '*') {
			_type = REM;
		} else {
			name = "!" + name;
			_type = ELELEMENT;
		}
		_name = name.substring(1);
	}

	/**
	 * name可以包含派生选择器，如："li strong",但getName()返回的是“li", getChildren()返回的是"strong"
	 * 
	 * @param css
	 *          属于哪个CSS文件
	 * @param name
	 *          标签名
	 * @param fromCssLine
	 *          来源行
	 */
	public CssLine(Css css, String name, CssLine fromCssLine) {
		this(css, name);
		for (AttrBase a : fromCssLine._as)
			add(a);
	}

	/**
	 * 将本标签的属性共享给其他标签，如果两个标签的属性一样，如：P,H5 {font-size: 1em;}, 在[P]建立后用本方法将P引用给H5
	 * 
	 * @param name
	 *          要引用的标签名
	 * @return 引用的标签的CssLine
	 */
	public CssLine link(String name) {
		CssLine css = new CssLine(null, name);
		css._as = _as;
		if (_links == null)
			_links = new Vector<CssLine>();
		_links.add(css);
		return css;
	}

	/**
	 * 将本标签的属性共享给其他标签，如果两个标签的属性一样，如：P,#H5 {font-size: 1em;}, 在[P]建立后用本方法将P引用给#H5
	 * 
	 * @param name
	 *          要引用的标签名
	 * @return 引用的标签的CssLine
	 */
	public CssLine linkId(String name) {
		return link(CssLine.ID + name);
	}

	/**
	 * 将本标签的属性共享给其他标签，如果两个标签的属性一样，如：P,.H5 {font-size: 1em;}, 在[P]建立后用本方法将P引用给.H5
	 * 
	 * @param name
	 *          要引用的标签名
	 * @return 引用的标签的CssLine
	 */
	public CssLine linkClazz(String name) {
		return link(CssLine.CLASS + name);
	}

	/**
	 * 取元素名
	 */
	public String getName() {
		return _name;
	}

	/**
	 * name可以包含派生选择器，如："li strong",但getName()返回的是“li", getChildren()返回的是"strong"
	 * 
	 * @return
	 */
	public String getChildren() {
		if (_children.length() > 1)
			return _children.substring(1);
		return _children;
	}

	public String getParent() {
		if (_parent.length() > 1)
			return _parent.substring(0, _parent.length() - 1);
		return _parent;
	}

	public CssLine setParent(String parent) {
		_parent = parent + " ";
		return this;
	}

	public CssLine setParent(CssLine parent) {
		return setParent(parent._type + parent._name);
	}

	// 返回类型，为ELELEMENT=""，CLASS="."，ID="#"
	public String getType() {
		return _type;
	}

	/**
	 * 确认为普通元素，否则出错
	 */
	public void assertElelement() {
		if (!_type.equals(ELELEMENT))
			throw LOG.err("assertElelement","引用的标签[{0}]不是CSS的元素类型!", getName());
	}
/**
 * 确认为Class类型，否则出错
 */
	public void assertClass() {
		if (!_type.equals(CLASS))
			throw LOG.err("assertClass","引用的标签[{0}]不是CSS的CLASS类型!", getName());
	}
	/**
	 * 确认为Id类型，否则出错
	 */

	public void assertId() {
		if (!_type.equals(ID))
			throw LOG.err("assertId","引用的标签[{0}]不是CSS的ID类型!", getName());
	}

	/**
	 * 输出Css代码
	 * @param tab 缩进的空格字符串
	 * @return 结果
	 */
	public String outCssCode(String tab) {
		String ln = LN;
		if (tab == null || tab.length() == 0) {
			tab = "";
			ln = "";
		}
		if (_type.equals(REM))
			return "/*" + _name + "*/" + LN;
		StringBuilder buf = new StringBuilder();
		if (_links != null) {
			// 输出共享的属性
			for (CssLine s : _links)
				buf.append("," + s._parent + s._type + s._name + s._children);
		}
		buf.append("{" + ln);
		for (AttrBase a : _as)
			buf.append(tab + a.outCssCode() + ";" + ln);
		return _parent + _type + _name + _children + " " + buf.toString() + "}"
		    + LN;
	}
/**
 * 取所属的Css对象
 * @return
 */
	public Css getCss() {
		return _css;
	}

	public CssLine setChildren(String children) {
		_children = " " + children;
		return this;
	}

	public Vector<CssLine> getLinks() {
		if (_links == null)
			return new Vector<CssLine>();
		return _links;
	}
}
