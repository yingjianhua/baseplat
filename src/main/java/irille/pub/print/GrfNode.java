/**
 * 
 */
package irille.pub.print;

import java.util.Vector;

import irille.pub.IPubVars;
import irille.pub.html.XmlNode.TabStruct;

/**
 * 报表XML节点
 * @author whx
 * 
 */
public class GrfNode<T extends GrfNode> implements IPubVars {
	public static final String SPACES = TabStruct.SPACES;

	protected boolean _outLn = true;
	private String _head;
	protected Vector _nodes = new Vector();
	private GrfNode _parent;

	public GrfNode(GrfNode parent, String head) {
		_parent = parent;
		_head = head;
	}

	public T addAttr(String name, Object value) {
		if (value != null)
			_nodes.add(new GrfAttr(name, value));
		return (T) this;
	}
	public T addExp(String name, Object value) {
		if (value != null)
			_nodes.add(new GrfAttr(name, new GrfExp(value)));
		return (T) this;
	}
	public T insertAttrToFirst(String name, Object value) {
		if (value != null)
			_nodes.insertElementAt(new GrfAttr(name, value),0);
		return (T) this;
	}
	public T insertExpToFirst(String name, Object value) {
		if (value != null)
			_nodes.insertElementAt(new GrfAttr(name, new GrfExp(value)),0);
		return (T) this;
	}

	/**
	 * 外部程序要复制新对象要调用copyNew方法
	 * 
	 * @param tb
	 * @return
	 */
	public T copyNew(GrfNode parent) {
		return copy((T) new GrfNode(parent, _head));
	}

	/**
	 * 被子类的New方法调用
	 * 
	 * @param newObj
	 * @return
	 */
	protected T copy(T newObj) {
		newObj._outLn = _outLn;
		for (Object node : _nodes)
			if (node instanceof GrfNode) {
				newObj._nodes.add(((GrfNode)node).copyNew(newObj));
			} else
				newObj._nodes.add(node);
		return (T) this;
	}

	public GrfNode AddNode(String str) {
		if (str != null)
			return Add(new GrfNode(this, str));
		return null;
	}

	public T setParent(GrfNode parent) {
		_parent = parent;
		return (T) this;
	}
	public <T extends GrfNode> T Add(T node) {
		if (node == null)
			return null;
		_nodes.add(node);
		return node;
	}
	public T add(T node) {
		if (node == null)
			return null;
		_nodes.add(node);
		return (T)this;
	}

	public <T extends GrfNode> T InsertToFirst(T node) {
		if (node == null)
			return null;
		_nodes.insertElementAt(node,0);
		return node;
	}
	public T insertToFirst(T node) {
		if (node == null)
			return null;
		_nodes.insertElementAt(node,0);
		return (T)this;
	}

	public String head() {
		return _head;
	}

	public String foot() {
		return "End";
	}
	public void outBefore1() {
	}

	public void outBefore2() {
	}

	private boolean _outBeforeCalled = false;

	public void out(StringBuilder buf, int tab) {
		if (!_outBeforeCalled) { // 调用一次outBefore方法
			_outBeforeCalled = true;
			outBefore1();
			outBefore2();
		}
		buf.append(getTab(tab) + head() + LN);
		for (Object obj : _nodes) {
			if (obj instanceof GrfAttr)
				((GrfAttr) obj).out(buf, tab + 1);
			else
				((GrfNode) obj).out(buf, tab + 1);
		}
		buf.append(getTab(tab) + foot() + LN);
	}

	public GrfMain root() {
		if(this instanceof GrfMain)
			return (GrfMain)this;
		return _parent.root();
	}
	
	/**
	 * 返回父节点
	 * 
	 * @return
	 */
	public GrfNode R() {
		return R(1);
	}

	/**
	 * 回退指定层节点
	 * 
	 * @param i
	 *          1表示为父节点
	 * @return
	 */
	public GrfNode R(int i) {
		if (i == 0)
			return this;
		return _parent.R(i - 1);
	}

	/**
	 * @return the parent
	 */
	public GrfNode getParent() {
		return _parent;
	}

	public static String getTab(int tab) {
		return SPACES.substring(0, tab);
	}

	public static class GrfAttr {
		private String _name;
		private Object _value;

		public GrfAttr(String name, Object value) {
			_name = name;
			_value = value;
		}

		public void out(StringBuilder buf, int tab) {
			if (_value == null)
				return;
			String v;
			if (_value instanceof String)
				v = "'" + _value + "'";
			else
				v = _value.toString();
			buf.append(getTab(tab) + _name + "=" + v + LN);
		}
	}

	public static class GrfExp {
		private Object _exp;

		public GrfExp(Object exp) {
			_exp = exp;
		}

		public void setExp(Object exp) {
			_exp=exp;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return _exp.toString();
		}
	}
}
