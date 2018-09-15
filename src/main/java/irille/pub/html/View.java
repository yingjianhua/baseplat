package irille.pub.html;

import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.html.Xhtml.Html;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Html 文本输出的缓冲类, 将不是DataNode类的节点转化为静态文本，DataNode的节点在输出前填入数据即可，以提高效率
 * 
 * @author whx
 * 
 */
public class View<T extends View> extends ViewBase<T> {
	private static final Log LOG= new Log(View.class);
	public static Hashtable<String, StaticCodeBuf> _staticCodeHash = new Hashtable<String, StaticCodeBuf>();
	// 用于管理每个类的静态代码是否已输出，内容为类名
	private static final HashSet<String> _outHtmled = new HashSet<String>();
	private static String _htmlLabel = "div";
	private Vector<Object> _nodeDatas = new Vector<Object>();

	public View(String key) {
		super(key);
	}

	public View() {
		super(null);
	}

	public View(String key, Object... staticDatas) {
		super(key);
		setData(key, staticDatas);
	}

	public T setHtmlLabel(String htmlLabel) {
		_htmlLabel = htmlLabel;
		return (T) this;
	}

	/**
	 * 初始化节点
	 */
	public void initNodes() {
		css();
	}

	public T addData(Object data) {
		_nodeDatas.add(data);
		return (T) this;
	}

	public Object getData(int id) {
		return _nodeDatas.get(id);
	}

	public int getDataSize() {
		return _nodeDatas.size();
	}

	public void clearData() {
		_nodeDatas.clear();
	}

	@Override
	public String getXmlLabel() {
		return _htmlLabel;
	}

	@Override
	protected void createXmlFoot(IXmlBuf buf, TabStruct tab) {
		if (getXmlLabel().length() == 0)
			return;
		super.createXmlFoot(buf, tab);
	}

	@Override
	protected void createXmlHead(IXmlBuf buf, TabStruct tab) {
		String rem=getClass().getName();
		if( this instanceof Html)
			rem=rem.substring(0, rem.length()-5); //去掉尾部的"$Html"
		if (!getKey().equals(""))
			rem+=":" + getKey();
		new Rem(rem).createXml(buf, tab.sub());
		tab.add();
		if (getXmlLabel().length() == 0)
			return;
		super.createXmlHead(buf, tab);
	}

	@Override
	public void outHtmlCode(StringBuilder buf, Xhtml html, Bean bean,
	    ViewBase view, TabStruct tab) {
		if (!isOutHtmled()) {
			createXml(view, tab);
		}
		// 输出所有节点的代码到BUF中
		setNodeData(html, bean);
		// 输出Html
		getStaticCodeBuf().outHtmlCode(buf, html, bean, this, tab);
	};

	public void setNodeData(Xhtml html, Bean bean) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createXml(IXmlBuf buf, TabStruct tab) {
		if (buf.isToStringOut()) { // toString方法调用的直接Html代码输出，即没有与数据节点的代码合并
//			if (!isOutHtmled())
			  clearChildNodesAndAttribute();   
				initNodes();
//			_outHtmled.add(getClazzName());
			super.createXml(buf, tab);
			return;
		}
		if (isOutHtmled())
			throw LOG.err("createXml","[View]类的[createXml]方法只能调用一次！否则会重复产生代码!");
		initNodes();
		_outHtmled.add(getClazzName());
		if (buf != this)
			buf.appendView(this, tab);
		super.createXml(this, tab);
	}

	/**
	 * 静态代码是否已输出
	 * 
	 * @return
	 */
	private boolean isOutHtmled() {
		return _outHtmled.contains(getClazzName());
	}

	private String getClazzName() {
		if(!( this instanceof Html))
			return getClass().getName();
		return getLinkCssXhtml().getClass().getName();
	}
	
	@Override
	protected StaticCodeBuf getStaticCodeBuf() {
		StaticCodeBuf buf = _staticCodeHash.get(getClass().getName());
		if (buf == null) {
			buf = new StaticCodeBuf();
			_staticCodeHash.put(getClass().getName(), buf);
		}
		return buf;
	}

	@Override
  public Xhtml getLinkCssXhtml() {
	  return null;
  }
}
