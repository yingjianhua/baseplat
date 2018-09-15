package irille.pub.html;

import irille.pub.FileTools;
import irille.pub.Str;
import irille.pub.html.Nodes.Base;
import irille.pub.html.Nodes.Div;
import irille.pub.html.Nodes.NodeNotClose;
import irille.pub.html.Nodes.Script;
import irille.pub.html.Nodes.Title;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Test;
import junit.framework.TestResult;

/**
 * Html文件的基类，用于产生Html文件，一般 一个Html文件包含多个View或ViewDynamic
 * 
 * @param <T>
 */
public class Xhtml<T extends Xhtml> implements Test {
	public static final String LN = Base.LN;
	private Html _html;
	private Head _head;
	private Body _body;
	private HttpServletRequest _request;
	private HttpServletResponse _response;

	// private UserSession _user;

	public static void main(String[] args) {
		Long time = System.currentTimeMillis();
		System.out.println(time);
		System.err.println(Integer.MIN_VALUE);
		// Xhtml html = new Xhtml("AA");
		// System.err.println(html.html().getCode());
		// System.out.println(html.toString(2));
	}

	public Xhtml(String name) {
		this(null, name);
	}

	public Xhtml(String code, String name) {
		_html = new Html(this, code, name);
		_head = _html.getHead();
		_body = _html.getBody();
		metaHttpEquiv("Content-Type", "text/html; CHARSET=utf-8");
//		metaHttpEquiv("Content-Type", "text/html; charset=gb2312");
		metaHttpEquiv("Content-Language", "zh-cn");
		_html.set("xmlns", "http://www.w3.org/1999/xhtml");
	}

	public String getCode() {
		return _html.getCode();
	}
	
	public String getName() {
		return _html.getName();
	}
	
	/**
	 * @param arg0
	 * @see junit.framework.Test#run(junit.framework.TestResult)
	 */
	public void run(TestResult arg0) {
		_html.toString(2);
	}

	public final void testCase() {
	}

	public T scriptJs(String... fileNames) {
		for (String file : fileNames)
			_head.add(new Script().setTypeJavaScript().setSrc(file));
		return (T) this;
	}

	/**
	 * 引用Css 文件
	 * 
	 * @param files
	 *          文件名列表
	 * @return 本对象
	 */
	public T linkCss(String... files) {
		for (String file : files)
			_head.add(new Link().set("href", file).set("rel", "stylesheet").set("type", "text/css"));
		return (T) this;
	}

	/**
	 * 引用Css 文件
	 * 
	 * @param files
	 *          文件名列表
	 * @return 本对象
	 */
	public T linkCss(Css... csses) {
		if (_html._linkCsses == null)
			_html._linkCsses = new Vector<Css>();
		for (Css css : csses) {
			linkCss(css.getFileName());
			_html._linkCsses.add(css);
		}
		return (T) this;
	}

	/**
	 * 设定meta标签行
	 * 
	 * @param httpEquiv
	 * @param content
	 * @return
	 */
	public T metaHttpEquiv(String httpEquiv, String content) {
		_head.add(new Meta().setHttpEquiv(httpEquiv).setContent(content));
		return (T) this;
	}

	public T metaKeywords(String keywords) {
		_head.add(new Meta().setName("Keywords").setContent(keywords));
		return (T) this;
	}

	public T metaDescription(String description) {
		_head.add(new Meta().setName("Description").setContent(description));
		return (T) this;
	}

	public T metaProperty(String property, String content) {
		_head.add(new Meta().setProperty(property).setContent(content));
		return (T) this;
	}

	public final Div Div() {
		return _body.Div();
	}

	public final Div Div(String clazz) {
		return _body.Div(clazz);
	}

	public final Div Div(CssLine... cssLines) {
		return _body.Div(cssLines);
	}

	public final Div DivId(String clazz) {
		return _body.DivId(clazz);
	}

	public final Div DivId(CssLine cssId) {
		return _body.DivId(cssId);
	}

	public void initNodes() {
	}

	public void initCss() {
	}

	public Css css() {
		return _html.css();
	}

	public T add(Node... nodes) {
		_body.add(nodes);
		return (T) this;
	}

	/**
	 * 取产生文件的根目录，没有重构的话默认为""
	 * 
	 * @return
	 */
	public String rootDir() {
		return "";
	}

	/**
	 * 输出文件
	 * 
	 * @param file
	 *          文件名
	 * @param step
	 *          缩进空格数
	 */
	public final void saveToFile(String file, int step) {
		System.out.println("产生文件:" + file);
		FileTools.writeStr(file, toString(step));
	}

	public String toString(int step) {
		return _html.toString(step);
	}

	@Override
	public String toString() {
		return toString(0);
	}

	/**
	 * @return
	 * @see junit.framework.Test#countTestCases()
	 */
	public int countTestCases() {
		return 1;
	}

	public Head head() {
		return _head;
	}

	public Body body() {
		return _body;
	}

	public Html html() {
		return _html;
	}

	public class Html extends View<Html> {
		private String DOCTYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0"
		    + " Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
		private Head _head;
		private Body _body = new Body();
		private Vector<Css> _linkCsses = null;
		private Xhtml _xhtml;

		public Html(Xhtml xhtml, String code, String name) {
			_head =new Head();
			add(_head);
			_xhtml = xhtml;
			if (code == null)
				code = this.getCode();
			_head.add(new Title()).add(name);
			// this.setAnchor(getCode(), name);
		}

		public final String getName() {
			return this.getAnchorName();
		}

		/**
		 * 取代码，返回类名最后一段，并去掉最后3个字符“HTM”
		 * 
		 * @return
		 */
		public final String getCode() { // TODO
			String code = Str.getClazzLastCode(this.getClass().getName());
			return code.substring(0, code.length() - 5); // 去掉尾部的"$Html"
		}

		public Vector<Css> linkCsses() {
			if (_linkCsses == null)
				return new Vector<Css>();
			return _linkCsses;
		}

		@Override
		public void initNodes() {
			super.initNodes();
			_xhtml.initNodes();
		}

		@Override
		public void initCss() {
			super.initCss();
			_xhtml.initCss();
		}

		@Override
		protected void createXmlHead(IXmlBuf buf, irille.pub.html.Node.TabStruct tab) {
			buf.appendStr(DOCTYPE + LN);
			_head.createXml(buf, tab);
			super.createXmlHead(buf, tab);
		}

		@Override
		protected void createXmlFoot(IXmlBuf buf, irille.pub.html.Node.TabStruct tab) {
			_body.createXml(buf, tab);
			super.createXmlFoot(buf, tab);
		}

		@Override
		public String getXmlLabel() {
			return "html";
		}

		@Override
		public Xhtml getLinkCssXhtml() {
			return _xhtml;
		}

		public Head getHead() {
			return _head;
		}

		public Body getBody() {
			return _body;
		}

		@Override
		public String htmlRem() {
			if (super.htmlRem().equals(Str.getClazzLastCode(getClass())))
				return Str.getClazzLastCode(Str.leftSub(getClass().getName(), 5));
			return super.htmlRem();
		}
	}

	public static class Head extends Node<Head> {
		@Override
		public String getXmlLabel() {
			return "head";
		}
	}

	public static class Body extends Node<Body> {
		private String _xmlLabel="body";
		@Override
		public String getXmlLabel() {
			return _xmlLabel;
		}
		public Body setXmlLabel(String xmlLabel) {
			_xmlLabel=xmlLabel;
			return this;
		}
	}

	public static class Link extends Node<Link> {
		@Override
		public String getXmlLabel() {
			return "link";
		}

		@Override
		public boolean isClose() {
			return false;
		}
	}

	// <meta> 元素可提供有关页面的元信息（meta-information），比如针对搜索引擎和更新频度的描述和关键词。
	// <meta> 标签位于文档的头部，不包含任何内容。<meta> 标签的属性定义了与文档相关联的名称/值对。
	public static class Meta extends NodeNotClose<Meta> {
		public Meta setContent(String str) {
			return set("content", str);
		}

		public Meta setHttpEquiv(String str) {
			return set("http-equiv", str);
		}

		public Meta setProperty(String str) {
			return set("property", str);
		}

		@Override
		public String getXmlLabel() {
			return "meta";
		}
	}

	public class ViewInner<T extends ViewInner> extends View<T> {
		public ViewInner(String key) {
			super(key);
		}

		public ViewInner() {
			super(null);
		}

		public ViewInner(String key, Object... staticDatas) {
			super(key, staticDatas);
		}

		@Override
		public Xhtml getLinkCssXhtml() {
			return Xhtml.this;
		}
	}

	public void setHttp(HttpServletRequest request, HttpServletResponse response) {
		_request = request;
		_response = response;
	}

	public HttpServletRequest getRequest() {
		return _request;
	}

	public HttpServletResponse getResponse() {
		return _response;
	}
	//
	// public UserSession getUser() {
	// checkLogin();
	// return _user;
	// }
	//
	// public boolean checkLogin() {
	// if (getRequest() == null)
	// return false;
	// if (_user == null)
	// _user = UserLoginManager.getLoginUser(getRequest(), getResponse(),
	// false);
	// if (_user != null)
	// Svr.getInstance().setUserIdInSession(_user.getId());
	// return _user != null;
	// }
	// public class ViewTmp extends ViewInner {
	// @Override
	// public void initCss() {
	// super.initCss();
	// }
	// @Override
	// public void setNodeData(Xhtml html, BeanBase bean) {
	// super.setNodeData(html, bean);
	// }
	// @Override
	// public void initNodes() {
	// super.initNodes();
	// }
	// }
}
