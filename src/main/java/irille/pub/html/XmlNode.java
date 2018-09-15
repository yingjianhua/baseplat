package irille.pub.html;

import irille.pub.IPubVars;
import irille.pub.Log;
import irille.pub.html.AttributeCss.AttrNode;
import irille.pub.svr.Env;

import java.util.Vector;


public abstract class XmlNode<T extends XmlNode, BASE extends XmlNode> implements IPubVars{
	private static final Log LOG = new Log(XmlNode.class);
	private Vector<AttrBase> _as = null;
	private Vector<Object> _nodes = new Vector<Object>(); // 内部节点
	protected BASE _parent = null;

	// 增加属性
	public T set(AttrBase... as) {
		if (_as == null)
			_as = new Vector<AttrBase>();
		for (AttrBase a : as)
			_as.add(a);
		return (T) this;
	}

	/**
	 * 增加节点类型的属性，在Jsp中有应用
	 * 
	 * @param node
	 * @return
	 */
	public T setNode(XmlNode node) {
		return set(new AttrNode(node));
	}

	/**
	 * 增加节点
	 * 
	 * @param nodes
	 *          节点列表
	 * @return 返回nodes中的最后一个格式
	 */
	public BASE add(BASE... nodes) {
		for (BASE node : nodes) {
			if (node == null)
				throw LOG.err("isNull", "节点的值不能为[null]!");
			node._parent = this;
			_nodes.add(node);
		}
		return nodes[nodes.length - 1];
	}

	/**
	 * 增加节点，节点最终输出为obj.toString()方法的内容
	 * 
	 * @param objs
	 *          对象列表
	 * @return 本对象
	 */
	public T add(Object... objs) {
		for (Object obj : objs) {
			if (obj == null)
				throw LOG.err("isNull", "节点的值不能为[null]!");
			if (obj instanceof XmlNode)
				((XmlNode) obj)._parent = (BASE) this;
			_nodes.add(obj);
		}
		return (T) this;
	}

	/**
	 * 返回深层的最后节点
	 * 
	 * @return
	 */
	public BASE last() {
		int i = _nodes.size();
		for (; i > 0; i--) {
			if (_nodes.get(i - 1) instanceof Node)
				return (BASE) ((BASE) (_nodes.get(i - 1))).last();
		}
		return (BASE) this;
	}

	/**
	 * 产生单行的注释
	 * 
	 * @param str
	 *          注释内容
	 * @return 本对象
	 */
	public T rem(String str) {
		new Rem(str).insertToParent(this);
		return (T) this;
	}

	public T insertToParent(BASE node) {
		node.add(this);
		return (T) this;
	}

	public T set(String name, Object value) {
		return set(new AttrCust(name, value.toString()));
	}

	public T text(String str) {
		add(new Text(str));
		return (T) this;
	}

	// 返回上一节点
	public BASE _R() {
		return _parent;
	}

	public void clearAllNodes() {
		_nodes = new Vector<Object>();
	}

	public BASE returnToClazz(Class clazz) {
		if (this.getClass().getName().equals(clazz.getName()))
			return _parent;
		if (_parent == null)
			throw LOG.err("returnToClazz", "类型为[{0}]的节点没找到，无法后退!", clazz.getName());
		return (BASE) _parent.returnToClazz(clazz);
	}

	public BASE returnToLabel(String xmlLabel) {
		if (getXmlLabel().equals(xmlLabel))
			return _parent;
		if (_parent == null)
			throw LOG.err("returnToLabel", "类型为[{0}]的节点没找到，无法后退!", xmlLabel);
		return (BASE) _parent.returnToLabel(xmlLabel);
	}

	public BASE _Root() {
		if (_parent == null)
			return (BASE) this;
		return (BASE) _parent._Root();
	}

	public BASE getParent() {
		return _parent;
	}

	public abstract String getXmlLabel();

	/**
	 * 取属性值
	 * 
	 * @param name
	 *          属性名
	 * @return 结果，没找到返回null
	 */
	public String getAttribute(String name) {
		if (_as == null)
			return null;
		for (AttrBase a : _as)
			if (a.getName().equals(name))
				return a.getValue();
		return null;
	}

	/**
	 * 清除所有节点与属性，在ViewDynamic中有应用，用于每次产生代码（没有静态代码）
	 */
	public final void clearChildNodesAndAttribute() {
		_nodes.clear();
		_as = null;
	}

	// 标签是否要关闭
	public boolean isClose() {
		return true;
	}

	public final Object[] getNodes() {
		Object[] nodes = new Object[_nodes.size()];
		_nodes.toArray(nodes);
		return nodes;
	}

	// public Vector<Attribute> getAttributes() {
	// return _as;
	// }

	protected static final boolean[] TRUE_TRUE = { true, true };
	protected static final boolean[] FALSE_FALSE = { false, false };
	protected static final boolean[] FALSE_TRUE = { false, true };

	// 取分隔符数组
	protected boolean[] getOutLnFlags() {
		return TRUE_TRUE;
	}

	// 产生标签前部分的HTML的静态文本
	protected void createXmlHead(IXmlBuf buf, TabStruct tab) {
		if (tab.isLastLn())
			buf.appendStr(tab.setLastLnFalse().getTab());
		// 输出命令
		buf.appendStr(getSign()[0] + getXmlLabel());
		// 输出属性
		if (_as != null) {
			buf.appendStr(" ");
			for (AttrBase a : _as)
				buf.appendStr(a.getName() + "=\"" + a.getValue() + "\" ");
		}
		// 命令右缀关闭
		if (isClose())
			buf.appendStr(getSign()[1]);
		else
			buf.appendStr(getSign()[2]);
		if (getOutLnFlags()[0]) {// 如果以换行结尾，则输出下一行的前部空白
			tab.setLastLnTrue();
			buf.appendStr(LN);
		}
	}

	private static final String[] SIGNS = new String[] { "<", ">", "/>", "</",
	    ">" };

	protected String[] getSign() {
		return SIGNS;
	}

	// 产生标签后部分的HTML的静态文本
	protected void createXmlFoot(IXmlBuf buf, TabStruct tab) {
		if (isClose()) {
			if (tab.isLastLn())
				buf.appendStr(tab.setLastLnFalse().getTab());
			buf.appendStr(getSign()[3] + getXmlLabel() + getSign()[4]);
			if (getOutLnFlags()[1]) { // 如果以换行结尾
				buf.appendStr(LN);
				tab.setLastLnTrue();
			}
		}
	}

	// 产生标签节点部分的HTML的静态文本
	protected void createXmlBody(IXmlBuf buf, TabStruct tab) {
		for (Object obj : _nodes) {
			if (obj instanceof XmlNode) {
				((XmlNode) obj).createXml(buf, tab);
			} else {
				if (tab.isLastLn()) { // 如果前有换行，则输出缩进的空格
					buf.appendStr(tab.add().getTab());
					tab.sub().setLastLnFalse();
				}
				buf.appendStr(tran(obj.toString()));
			}
		}
	}

	/**
	 * 产生HTML的静态文本后，再添加节点将不影响输出结果！！！因为所有在标签已转换到静态缓冲中，结合数据就直接产生最后结果
	 * 
	 * @param buf
	 *          区域的视图，用于保存区域的静态文本及DataNode，
	 * @param tab
	 *          缩进空格
	 */
	public void createXml(IXmlBuf buf, TabStruct tab) {
		if (getXmlLabel().length() > 0)
			tab.add();
		createXmlHead(buf, tab);
		createXmlBody(buf, tab);
		createXmlFoot(buf, tab);
		if (getXmlLabel().length() > 0)
			tab.sub();
	}

	public final void createXml(IXmlBuf buf, int step) {
		createXml(buf, new TabStruct(step));
	}

	public String toString(int step) {
		ToStringBuf buf = new ToStringBuf();
		createXml(buf, new TabStruct(step));
		return buf.toString();
	}

	@Override
	public String toString() {
		return toString(0);
	}

	public int sizeNode() {
		return _nodes.size();
	}

	public int sizeAttribute() {
		return _as.size();
	}

	public final static String tran(String text) {
		StringBuilder buf = new StringBuilder();
		for (char ch : text.toCharArray()) {
			if (ch == ' ')
				buf.append("&nbsp;");
			else if (ch == '\t')
				buf.append("&nbsp;&nbsp;"); // Tab换为２个空格
			else if (ch == '&')
				buf.append("&amp;");
			else if (ch == '<')
				buf.append("&lt;");
			else if (ch == '>')
				buf.append("&gt;");
			else if (ch == '"')
				buf.append("&quot;");
			else if (ch == '\'')
				buf.append("&apos;");
			else if (ch == '\n')
				buf.append("<br />\n");
			else
				buf.append(ch);
		}
		return buf.toString();
	}

	/**
	 * 缩进空格管理的表结构
	 * 
	 * @author whx
	 * 
	 */
	public static class TabStruct {
		private boolean _lastLn = false;
		private int _step;
		private int _tab = 0;
		public static final String SPACES = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
		    + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";

		public TabStruct(int step) {
			_step = step;
			_tab = -step;
		}

		public TabStruct add() {
			_tab += _step;
			return this;
		}

		public TabStruct sub() {
			_tab -= _step;
			return this;
		}

		public String getTab() {
			return SPACES.substring(0, _tab);
		}

		public boolean isLastLn() {
			return _lastLn;
		}

		public TabStruct setLastLnFalse() {
			_lastLn = false;
			return this;
		}

		public TabStruct setLastLnTrue() {
			_lastLn = true;
			return this;
		}
	}

	/**
	 * 本文输出的节点，对数据不进行转换
	 * 
	 * @author whx
	 * @param <T>
	 */
	private static class Text<T extends Text> extends XmlNode<T, XmlNode> {
		protected String _str;

		public Text(String str) {
			_str = str;
		}

		@Override
		protected void createXmlHead(IXmlBuf buf,
		    irille.pub.html.XmlNode.TabStruct tab) {
		}

		@Override
		protected void createXmlBody(IXmlBuf buf, TabStruct tab) {
			buf.appendStr(_str);
		}

		@Override
		protected void createXmlFoot(IXmlBuf buf,
		    irille.pub.html.XmlNode.TabStruct tab) {
		}

		@Override
		public String getXmlLabel() {
			return "";
		}
	}

	public static class Rem<T extends Rem> extends XmlNode<T, XmlNode> {
		protected String _str;

		public Rem(String str) {
			_str = str;
		}

		@Override
		protected void createXmlHead(IXmlBuf buf, TabStruct tab) {
			if (!tab.isLastLn()) {
				buf.appendStr(LN + tab.setLastLnTrue().getTab());
			} else {
				buf.appendStr(tab.getTab());
			}
			buf.appendStr("<!-- " + _str + " -->" + LN);
		}

		@Override
		protected void createXmlFoot(IXmlBuf buf, TabStruct tab) {
			return;
		}

		@Override
		public boolean isClose() {
			return false;
		}

		@Override
		public String getXmlLabel() {
			return _str;
		}
	}

	public static class ToStringBuf implements IXmlBuf {
		private StringBuilder buf = new StringBuilder();

		public IXmlBuf appendView(ViewBase view, TabStruct tab) {
			throw LOG.err("ToStringBufView", "用toString的节点不能有子节点类型为“View”的对象!");
		}

		public IXmlBuf appendVirtualNode(NodeVirtual dataNode) {
			throw LOG.err("ToStringBufNodeVirtual",
			    "用toString的节点不能有子节点类型为“NodeVirtual”的对象!");
		}

		public IXmlBuf appendStr(String str) {
			buf.append(str);
			return this;
		}

		public boolean isToStringOut() {
			return true;
		}

		@Override
		public String toString() {
			return buf.toString();
		}
	}
}
