package irille.pub.html;

import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.html.Nodes.Div;
import irille.pub.html.Nodes.StyleNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;


/**
 * Html 文本输出的缓冲类, 将不是DataNode类的节点转化为静态文本，DataNode的节点在输出前填入数据即可，以提高效率
 * 
 * @author whx
 * 
 */
public abstract class ViewBase<T extends ViewBase> extends Div<T> implements
    IXmlBuf<T> {
	private String _key; // 用于取数据的键值
	private static final HashMap<Class, Css> CSSES = new HashMap<Class, Css>();
	private static final HashMap<Class, HashMap<String, Object>> DATAS = new HashMap<Class, HashMap<String, Object>>();
	private String _htmlRem = null;

	public ViewBase(String key) {
		if (key == null)
			_key = "";
		else
			_key = key;
	}

	/**
	 * 取数据
	 * 
	 * @param key
	 *          键值
	 * @return 如没有数据，则返回""
	 */
	public Object getData(String key) {
		HashMap<String, Object> map = DATAS.get(getClass());
		if (map == null)
			return "";
		Object obj = map.get(key);
		if (obj == null)
			return "";
		return obj;
	}

	/**
	 * 取当前对象的静态数据
	 * 
	 * @return 如没有数据，则返回""
	 */
	public Object getData() {
		return getData(_key);
	}

	public void setData(String key, Object... values) {
		HashMap<String, Object> map = DATAS.get(getClass());
		if (map == null) {
			map = new HashMap<String, Object>();
			DATAS.put(getClass(), map);
		}
		map.put(key, values);
	}

	/**
	 * 取本类的Css定义对象
	 * 
	 * @return
	 */
	public Css css() {
		Css css = CSSES.get(getClass());
		if (css == null) {
			css = new Css().setFileName(getClass().getName());
			CSSES.put(getClass(), css);
			initCss();
		}
		return css;
	}

	/**
	 * 初始化Css，本方法每个类会自动调用一次，外部方法不可以调用本方法，否则会死循环！
	 */
	public void initCss() {
	}

	/**
	 * 取Css引用的Xhtml文件，对于模块化的View需要指定Xhtml，在其它Xhtml中引用时自动加入Css的引用行
	 * 
	 * @return Xhtml对象
	 */
	public abstract Xhtml getLinkCssXhtml();

	/**
	 * 产生Html代码到Buf中
	 * 
	 * @param buf
	 *          输出最终结果的文本缓冲区
	 * @param html
	 *          页面，页面ID将被各超连接引用
	 * @param bean
	 *          数据对象，与View中的DataNode结合产生Html动态文本
	 * @param view
	 *          视图
	 * @param tab
	 *          缩进的空格
	 */
	public abstract void outHtmlCode(StringBuilder buf, Xhtml html,
	    Bean bean, ViewBase view, TabStruct tab);

	public final void outHtmlCode(StringBuilder buf, Xhtml html, ViewBase view,
	    Bean bean) {
		outHtmlCode(buf, html, bean, view, new TabStruct(0));
	}

	// 如果是动态的数据节点，保存节点，动态输入数据
	protected abstract StaticCodeBuf getStaticCodeBuf();

	// 如果是动态的数据节点，保存节点，动态输入数据
	public final T appendView(ViewBase view, TabStruct tab) {
		getStaticCodeBuf().append(view);
		return (T) this;
	}

	/**
	 * 
	 * 检查id与class有没有在Css文件中定义，在继承的类中要重构方法getLinkCssXhtml()，用于指明所在的Xhtml文件，以引用link的所有Css文件
	 * 
	 * @return
	 */
	public String checkCss() {
		StringBuilder buf = new StringBuilder();
		HashSet<String> elements = new HashSet<String>();
		Vector<Css> csses = new Vector<Css>();
		if (getLinkCssXhtml() != null)
			csses.addAll(getLinkCssXhtml().html().linkCsses());
		csses.add(0, css());
		Css[] ac = new Css[csses.size()];
		csses.toArray(ac);
		checkNodeCss(buf, this, elements, ac);
		return buf.toString();
	}

	private void checkNodeCss(StringBuilder buf, Node node,
	    HashSet<String> elements, Css... csses) {
		checkValueCss(buf, node.getAttribute("class"), "class", elements, csses);
		checkValueCss(buf, node.getAttribute("id"), "id", elements, csses);
		for (Object obj : node.getNodes()) {
			if (obj instanceof Node) {
				checkNodeCss(buf, (Node) obj, elements, csses);
			}
		}
	}

	private void checkValueCss(StringBuilder buf, String value, String idOrClazz,
	    HashSet<String> elements, Css... csses) {
		if (value == null)
			return;
		String[] vs = value.split(" ");
		boolean ok;
		for (String v : vs) {
			if (elements.contains(v) || v.length() == 0)
				continue;
			elements.add(v);
			ok = false;
			for (Css css : csses) {
				if (idOrClazz.equals("id")) {
					if (css.getId(v) != null) {
						buf.append(idOrClazz + ":" + v + "-->" + css.getFileName() + LN);
						ok = true;
						break;
					}
				} else {
					if (css.getClazz(v) != null) {
						buf.append(idOrClazz + ":" + v + "-->" + css.getFileName() + LN);
						ok = true;
						break;
					}
				}
			}
			if (!ok)
				buf.append(idOrClazz + ":" + v + ":没定义!!!!!!!!!!!!" + LN);
		}
	}

	// 增加虚拟节点，用于存放数据
	public final T appendVirtualNode(NodeVirtual dataNode) {
		getStaticCodeBuf().append(dataNode);
		return (T) this;
	}

	public final T appendStr(String str) {
		getStaticCodeBuf().append(str);
		return (T) this;
	}

	public final String getKey() {
		return _key;
	}

	@Override
	protected void createXmlHead(IXmlBuf buf, irille.pub.html.Node.TabStruct tab) {
		if (css().size() != 0) { // 先输出CSS代码
			new StyleNode(htmlRem(), css()).createXml(buf, tab.sub());
			tab.add();
		}
		super.createXmlHead(buf, tab);
	}

	/**
	 * Css 节点输出时的备注信息
	 * 
	 * @return
	 */
	public String htmlRem() {
		if (_htmlRem == null)
			return Str.getClazzLastCode(getClass());
		return _htmlRem;
	}

	public T setHtmlRem(String htmlRem) {
		_htmlRem = htmlRem;
		return (T) this;
	}

	public boolean isToStringOut() {
		return false;
	}

	protected static final class StaticCodeBuf {
		private StringBuilder _buf = new StringBuilder();
		private Vector<String> _beforeStrs = new Vector<String>();
		private Vector<Node> _nodes = new Vector<Node>();

		public void append(String str) {
			_buf.append(str);
		}

		public void append(Node node) {
			_nodes.add(node);
			_beforeStrs.add(_buf.toString());
			_buf.delete(0, _buf.length());
		}

		public void outHtmlCode(StringBuilder buf, Xhtml html, Bean bean,
		    View view, TabStruct tab) {
			Node node;
			Object obj;
			int id = 0; // 当前数据的位置
			ViewDynamic dyView = new ViewDynamic(null);
			for (int i = 0; i < _nodes.size(); i++) {
				node = _nodes.get(i);
				buf.append(_beforeStrs.get(i));
				if (node instanceof ViewBase) {
					((ViewBase) node).outHtmlCode(buf, html, bean, view, tab);
				} else {
					if (view.getDataSize() < id + 1) // 没有设置动态数据，不输出！
						continue;
					obj = view.getData(id);
					id++;
					if (obj instanceof Node) {// 动态节点
						dyView.add((Node) obj);
						dyView.outHtmlCode(buf, html, null, view, tab);
					} else { // 动态数据
						buf.append(obj.toString());
					}
				}
			}
			buf.append(_buf);
		}
	}
}
