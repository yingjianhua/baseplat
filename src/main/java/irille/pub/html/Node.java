package irille.pub.html;

import irille.pub.FileTools;
import irille.pub.Log;
import irille.pub.html.Nodes.A;
import irille.pub.html.Nodes.Area;
import irille.pub.html.Nodes.B;
import irille.pub.html.Nodes.Base;
import irille.pub.html.Nodes.Big;
import irille.pub.html.Nodes.Br;
import irille.pub.html.Nodes.Button;
import irille.pub.html.Nodes.Caption;
import irille.pub.html.Nodes.Col;
import irille.pub.html.Nodes.CssNode;
import irille.pub.html.Nodes.Dd;
import irille.pub.html.Nodes.Dir;
import irille.pub.html.Nodes.Div;
import irille.pub.html.Nodes.Dl;
import irille.pub.html.Nodes.Dt;
import irille.pub.html.Nodes.Em;
import irille.pub.html.Nodes.Fieldset;
import irille.pub.html.Nodes.Form;
import irille.pub.html.Nodes.Frame;
import irille.pub.html.Nodes.Frameset;
import irille.pub.html.Nodes.H1;
import irille.pub.html.Nodes.H2;
import irille.pub.html.Nodes.H3;
import irille.pub.html.Nodes.H4;
import irille.pub.html.Nodes.H5;
import irille.pub.html.Nodes.H6;
import irille.pub.html.Nodes.Hr;
import irille.pub.html.Nodes.I;
import irille.pub.html.Nodes.Img;
import irille.pub.html.Nodes.Input;
import irille.pub.html.Nodes.Label;
import irille.pub.html.Nodes.Legend;
import irille.pub.html.Nodes.Li;
import irille.pub.html.Nodes.Map;
import irille.pub.html.Nodes.Menu;
import irille.pub.html.Nodes.NodeCust;
import irille.pub.html.Nodes.Noframes;
import irille.pub.html.Nodes.ObjectH;
import irille.pub.html.Nodes.Ol;
import irille.pub.html.Nodes.Optgroup;
import irille.pub.html.Nodes.Option;
import irille.pub.html.Nodes.P;
import irille.pub.html.Nodes.Param;
import irille.pub.html.Nodes.Pre;
import irille.pub.html.Nodes.Q;
import irille.pub.html.Nodes.Script;
import irille.pub.html.Nodes.Select;
import irille.pub.html.Nodes.Small;
import irille.pub.html.Nodes.Span;
import irille.pub.html.Nodes.StyleNode;
import irille.pub.html.Nodes.Sub;
import irille.pub.html.Nodes.Sup;
import irille.pub.html.Nodes.Table;
import irille.pub.html.Nodes.Tbody;
import irille.pub.html.Nodes.Td;
import irille.pub.html.Nodes.Temp;
import irille.pub.html.Nodes.Textarea;
import irille.pub.html.Nodes.Tfoot;
import irille.pub.html.Nodes.Th;
import irille.pub.html.Nodes.Thead;
import irille.pub.html.Nodes.Title;
import irille.pub.html.Nodes.Tr;
import irille.pub.html.Nodes.Tt;
import irille.pub.html.Nodes.U;
import irille.pub.html.Nodes.Ul;
import irille.pub.svr.Env;

/**
 * HTML 中格式的基类，所有大写开头的方法用于产生同名节点，并返回节点对象； 小写字母开头的为普通方法，返回值为当前对象； _R() 方法返回上级节点。
 * 
 * @author whx
 * 
 * @param <T>
 */
public abstract class Node<T extends Node> extends XmlNode<T, Node> {
	private static final Log LOG = new Log(Node.class);
	public static final String JS = "text/javascript";
	private Anchor _anchor = null;

	@Override
	public T set(AttrBase... as) {
		super.set(as);
		return (T) this;
	}

	public T set(String name, String value) {
		return set(new AttrCust(name, value));
	}

	@Override
	public Node add(Node... nodes) {
		return (Node) super.add(nodes);
	}

	/**
	 * 加入的对象将对空格等字符进行转换再输出
	 */
	@Override
	public T add(java.lang.Object... objs) {
		super.add(objs);
		return (T) this;
	}

	@Override
	public Node last() {
		super.last();
		return (T) this;
	}

	@Override
	public T rem(String str) {
		super.rem(str);
		return (T) this;
	}

	@Override
	public T insertToParent(Node node) {
		node.add(this);
		return (T) this;
	}

	@Override
	public T set(String name, Object value) {
		if (value == null)
			return (T) this;
		super.set(name, value);
		return (T) this;
	}

	public T setOnclickVm(String vmFile) {
		return set("onclick", "javascript:window.location.href='" + vmFile + "';");
	}

	public T setOnsubmit(String submit) {
		return set("onsubmit", submit);
	}

	@Override
	public T text(String str) {
		super.text(str);
		return (T) this;
	}

	@Override
	public Node _R() {
		return (Node) super._R();
	}

	@Override
	public Node returnToClazz(Class clazz) {
		super.returnToClazz(clazz);
		return (T) this;
	}

	@Override
	public Node returnToLabel(String xmlLabel) {
		super.returnToLabel(xmlLabel);
		return (T) this;
	}

	@Override
	public Node _Root() {
		super._Root();
		return (T) this;
	}

	/**
	 * 增加自定义名称的节点，标签被设定为需要关闭
	 * 
	 * @param name
	 *          节点的XML标签名称
	 * @return 节点对象
	 */
	public final NodeCust addNode(String name) {
		return new NodeCust(name);
	}

	/**
	 * 增加自定义名称的节点，标签被设定为不需要关闭
	 * 
	 * @param name
	 *          节点的XML标签名称
	 * @return 节点对象
	 */
	public final NodeCust addNodeNotClose(String name) {
		return new NodeCust(name, false);
	}

	public T lineSpace(int px) {
		Div("lineSpace" + px);
		return (T) this;
	}

	public final A A() {
		return (A) new A().insertToParent(this);
	}

	public final A A(String href) {
		return (A) new A().setHref(href).insertToParent(this);
	}

	public final Area Area() {
		return (Area) new Area().insertToParent(this);
	}

	public final B B() {
		return (B) new B().insertToParent(this);
	}

	public final Base Base() {
		return (Base) new Base().insertToParent(this);
	}

	public final Big Big() {
		return (Big) new Big().insertToParent(this);
	}

	public final T br() {
		return (T) new Br().insertToParent(this)._R();
	}

	public final T br(int count) {
		for (int i = 0; i < count; i++)
			br();
		return (T) this;
	}

	public final Button Button() {
		return (Button) new Button().insertToParent(this);
	}

	public final Button Button(CssLine... cssLines) {
		return (Button) new Button().setClazz(cssLines).insertToParent(this);
	}

	public final Caption Caption(String string) {
		return (Caption) new Caption(string).insertToParent(this);
	}

	public final Col Col() {
		return (Col) new Col().insertToParent(this);
	}

	public final Dd Dd() {
		return (Dd) new Dd().insertToParent(this);
	}

	public final Dir Dir() {
		return (Dir) new Dir().insertToParent(this);
	}

	public final Div Div() {
		return (Div) new Div().insertToParent(this);
	}

	public final Div Div(String clazz) {
		return (Div) new Div().setClazz(clazz).insertToParent(this);
	}

	public final Div Div(CssLine... cssLines) {
		return (Div) new Div().setClazz(cssLines).insertToParent(this);
	}

	public final Input Input(CssLine... cssLines) {
		return (Input) new Input().setClazz(cssLines).insertToParent(this);
	}

	public final Div DivId(String clazz) {
		return (Div) new Div().setId(clazz).insertToParent(this);
	}

	public final Div DivId(CssLine cssId) {
		return (Div) new Div().setId(cssId).insertToParent(this);
	}

	public final Dl Dl() {
		return (Dl) new Dl().insertToParent(this);
	}

	public final Dt Dt() {
		return (Dt) new Dt().insertToParent(this);
	}

	public final Fieldset Fieldset(String legendString) {
		return (Fieldset) new Fieldset(legendString).insertToParent(this);
	}

	public final Form Form(String url) {
		return (Form) new Form(url).insertToParent(this);
	}

	public final Frame Frame() {
		return (Frame) new Frame().insertToParent(this);
	}

	public final Frameset Frameset() {
		return (Frameset) new Frameset().insertToParent(this);
	}

	public final Noframes Noframes() {
		return (Noframes) new Noframes().insertToParent(this);
	}

	public final H1 H1() {
		return (H1) new H1().insertToParent(this);
	}

	public final H1 H1(String text) {
		return (H1) H1().add(text);
	}

	public final H2 H2() {
		return (H2) new H2().insertToParent(this);
	}

	public final H2 H2(String text) {
		return (H2) H2().add(text);
	}

	public final H3 H3() {
		return (H3) new H3().insertToParent(this);
	}

	public final H3 H3(String text) {
		return (H3) H3().add(text);
	}

	public final H4 H4() {
		return (H4) new H4().insertToParent(this);
	}

	public final H4 H4(String text) {
		return (H4) H4().add(text);
	}

	public final H5 H5() {
		return (H5) new H5().insertToParent(this);
	}

	public final H5 H5(String text) {
		return (H5) H5().add(text);
	}

	public final H6 H6() {
		return (H6) new H6().insertToParent(this);
	}

	public final H6 H6(String text) {
		return (H6) H6().add(text);
	}

	/**
	 * 画一横线
	 * 
	 * @return
	 */
	public final T hr() {
		return (T) new Hr().insertToParent(this)._R();
	}

	public final I I() {
		return (I) new I().insertToParent(this);
	}

	public final T img(String src) {
		return (T) new Img(src).insertToParent(this)._R();
	}

	public final Img Img(String src) {
		return (Img) new Img(src).insertToParent(this);
	}

	public final Img Img(String src, String alt) {
		return (Img) new Img(src, alt).insertToParent(this);
	}

	public final Label Label(String forId, String labelString) {
		return (Label) new Label(forId, labelString).insertToParent(this);
	}

	public final Label Label(String labelString) {
		return (Label) new Label(labelString).insertToParent(this);
	}

	public final Input Input() {
		return (Input) new Input().insertToParent(this);
	}

	public final Legend Legend(String text) {
		return (Legend) new Legend(text).insertToParent(this);
	}

	public final Li Li() {
		return (Li) new Li().insertToParent(this);
	}

	public final Map Map(String id) {
		return (Map) new Map(id).insertToParent(this);
	}

	public final Menu Menu() {
		return (Menu) new Menu().insertToParent(this);
	}

	public final Object Object() {
		return (ObjectH) new ObjectH().insertToParent(this);
	}

	public final Ol Ol() {
		return (Ol) new Ol().insertToParent(this);
	}

	public final Optgroup Optgroup(String labelText) {
		return (Optgroup) new Optgroup(labelText).insertToParent(this);
	}

	public final Option Option() {
		return (Option) new Option().insertToParent(this);
	}

	public final P P() {
		return (P) new P().insertToParent(this);
	}

	public final P P(String text) {
		return (P) P().add(text);
	}

	public final P P2(String text) {
		return (P) P().add("　　" + text);
	}

	public final T p() {
		new P().insertToParent(this);
		return (T) this;
	}

	public final T p(Object text) {
		new P().add(text).insertToParent(this);
		return (T) this;
	}

	public final T p2(Object text) {
		new P().add("　　").add(text).insertToParent(this);
		return (T) this;
	}

	public final Param Param(String name) {
		return (Param) new Param(name).insertToParent(this);
	}

	public final Pre Pre() {
		return (Pre) new Pre().insertToParent(this);
	}

	public final Q Q() {
		return (Q) new Q().insertToParent(this);
	}

	public final Script Script() {
		return (Script) new Script().insertToParent(this);
	}

	public final Select Select() {
		return (Select) new Select().insertToParent(this);
	}

	public final Select Select(CssLine... cssLines) {
		return (Select) new Select().setClazz(cssLines).insertToParent(this);
	}

	public final Small Small() {
		return (Small) new Small().insertToParent(this);
	}

	public final Span Span() {
		return (Span) new Span().insertToParent(this);
	}

	public Span Span(String fileDir, String code, String name) {
		return (Span) Span().setAnchor(fileDir, code, name).add(name);
	}

	public final Span Span(Object text) {
		return (Span) Span().add(text.toString());
	}

	public final StyleNode StyleNode(String rem, Css css) {
		return (StyleNode) new StyleNode(rem, css).insertToParent(this);
	}

	public final Sub Sub() {
		return (Sub) new Sub().insertToParent(this);
	}

	public final Sup Sup() {
		return (Sup) new Sup().insertToParent(this);
	}

	public final Table Table() {
		return (Table) new Table().insertToParent(this);
	}

	public final Tbody Tbody() {
		return (Tbody) new Tbody().insertToParent(this);
	}

	public final Td Td() {
		return (Td) new Td().insertToParent(this);
	}

	public final Temp Temp() {
		return (Temp) new Temp().insertToParent(this);
	}

	public final Textarea Textarea(int cols, int rows) {
		return (Textarea) new Textarea(cols, rows).insertToParent(this);
	}

	public final Textarea Textarea() {
		return (Textarea) new Textarea().insertToParent(this);
	}

	public final Tfoot Tfoot() {
		return (Tfoot) new Tfoot().insertToParent(this);
	}

	public final Th Th() {
		return (Th) new Th().insertToParent(this);
	}

	public final Thead Thead() {
		return (Thead) new Thead().insertToParent(this);
	}

	public final Title Title() {
		return (Title) new Title().insertToParent(this);
	}

	public final Tr Tr() {
		return (Tr) new Tr().insertToParent(this);
	}

	public final Tt Tt() {
		return (Tt) new Tt().insertToParent(this);
	}

	public final U U() {
		return (U) new U().insertToParent(this);
	}

	public final Ul Ul() {
		return (Ul) new Ul().insertToParent(this);
	}

	public final Em Em(String clazz) {
		return (Em) new Em(clazz).insertToParent(this);
	}

	public final T em(String clazz) {
		new Em(clazz).insertToParent(this);
		return (T) this;
	}

	public final Em Em() {
		return (Em) new Em().insertToParent(this);
	}

	public final T setAnchor(String fileDir, String code, String name) {
		_anchor = new Anchor(fileDir, code, name);
		return (T) this;
	}

	public final T setAnchor(Class clazz, String code, String name) {
		return setAnchor(FileTools.tranClassToFileDir(clazz), code, name);
	}

	public final T addUrl(String callFileDir, Node node) { 
		add(new A().setHref(assertAnchor().getUrl(callFileDir)).add(
				node.getAnchorName()));
		return (T) this;
	}

	public final T addUrl(Class callClazz, Node node) {
		return addUrl(FileTools.tranClassToFileDir(callClazz),node);
	}

	public final T setId(String value) {
		return set(new AttrCust("id", value));
	}

	public final T setName(String value) {
		return set(new AttrCust("name", value));
	}

	public final T setTitle(String value) {
		return set(new AttrCust("title", value));
	}

	public final T setStyle(AttrBase... as) {
		return set(new AttrCust("style", new Style(as).getAttributes()));
	}

	public final T setStyle(String str) {
		return set(new AttrCust("style", str));
	}

	// 增加Css 中 元素的引用, 相当于增加格式
	public final Node setElement(CssLine cssLine) {
		cssLine.assertElelement();
		return add(new CssNode(cssLine));
	}

	// 增加Css 中 id的引用, 相当于增加属性
	public final T setId(CssLine cssLine) {
		cssLine.assertId();
		return set(new Id(cssLine));
	}

	// 增加Css 中 class的引用, 相当于增加属性
	public final T setClazz(CssLine... cssLines) {
		String s = "";
		for (CssLine line : cssLines) {
			line.assertClass();
			s = s + " " + line.getName();
		}
		return set(new Clazz(s.substring(1)));
	}

	// 设定初始值
	public T setValue(Object value) {
		return set("value", value.toString());
	}

	// 增加Css 中 class的引用, 相当于增加属性
	public final T setClazz(String clazz) {
		if (clazz == null || clazz.isEmpty())
			return (T) this;
		return set(new Clazz(clazz));
	}

	/**
	 * 当元素改变时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onchange(String str) {
		return set(new AttrCust("onchange", str));
	}

	/**
	 * 当表单被提交时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onsubmit(String str) {
		return set(new AttrCust("onsubmit", str));
	}

	/**
	 * 当表单被重置时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onreset(String str) {
		return set(new AttrCust("onreset", str));
	}

	/**
	 * 当元素被选取时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onselect(String str) {
		return set(new AttrCust("onselect", str));
	}

	/**
	 * 当元素失去焦点时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onblur(String str) {
		return set(new AttrCust("onblur", str));
	}

	/**
	 * 当元素获得焦点时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onfocus(String str) {
		return set(new AttrCust("onfocus", str));
	}

	/**
	 * 当图像加载中断时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onabort(String str) {
		return set(new AttrCust("onabort", str));
	}

	/**
	 * 当键盘被按下时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onkeydown(String str) {
		return set(new AttrCust("onkeydown", str));
	}

	/**
	 * 当键盘被按下后又松开时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onkeypress(String str) {
		return set(new AttrCust("onkeypress", str));
	}

	/**
	 * 当键盘被松开时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onkeyup(String str) {
		return set(new AttrCust("onkeyup", str));
	}

	/**
	 * 当鼠标被单击时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onclick(String str) {
		return set(new AttrCust("onclick ", str));
	}

	/**
	 * 当鼠标被双击时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T ondblclick(String str) {
		return set(new AttrCust("ondblclick", str));
	}

	/**
	 * 当鼠标按钮被按下时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onmousedown(String str) {
		return set(new AttrCust("onmousedown", str));
	}

	/**
	 * 当鼠标指针移动时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onmousemove(String str) {
		return set(new AttrCust("onmousemove", str));
	}

	/**
	 * 当鼠标指针移出某元素时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onmouseout(String str) {
		return set(new AttrCust("onmouseout", str));
	}

	/**
	 * 当鼠标指针悬停于某元素之上时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onmouseover(String str) {
		return set(new AttrCust("onmouseover", str));
	}

	/**
	 * 当鼠标按钮被松开时执行脚本
	 * 
	 * @param str
	 * @return
	 */
	public final T onmouseup(String str) {
		return set(new AttrCust("onmouseup", str));
	}

	public final Node _Tr() {
		return returnToClazz(Tr.class);
	}

	public final Node _Table() {
		return returnToClazz(Table.class);
	}

	public final Node _Div() {
		return returnToClazz(Div.class);
	}

	public final Node _Td() {
		return returnToClazz(Td.class);
	}

	public final Node _Form() {
		return returnToClazz(Form.class);
	}

	public final Node _Ul() {
		return returnToClazz(Ul.class);
	}

	public final Node _Dl() {
		return returnToClazz(Dl.class);
	}

	// public final Node _Tr() {
	// return returnToClazz(Tr.class);
	// }

	// 取锚的名称
	public final String getAnchorName() {
		return assertAnchor()._name;
	}

	// 取锚的代码
	public final String getAnchorCode() {
		return assertAnchor()._code;
	}

	public final Anchor getAnchor() {
		return _anchor;
	}

	public final Anchor assertAnchor() {
		if (_anchor == null)
			throw LOG.err("anchorErr", "本节点的“ancher”属性为null值。");
		return _anchor;
	}

	@Override
	protected void createXmlHead(IXmlBuf buf, irille.pub.html.XmlNode.TabStruct tab) {
		super.createXmlHead(buf, tab);
		// 输出锚的定义
		if (_anchor != null)
			new A().setId(_anchor.getCode()).createXml(buf, tab);
	}

	// public String getName() {
	// String[] ss=getClass().getName().split(".");
	// return ss[ss.length-1].toLowerCase();
	// }

	// protected String getSrcBody(String tab, String step) {
	// StringBuilder buf = new StringBuilder();
	// if (getOutLnFlags()[0]) //
	// buf.append(step);
	// for (Object obj : _nodes) {
	// if (obj instanceof Node) {
	// ((Node) obj).outSrc(buf, tab + step, step);
	// } else {
	// String ts = tab + step;
	// buf.append(obj.toString());
	// String rem = LN + (ts.length() == 0 ? ts : ts.substring(step.length()));
	// buf.append(rem);
	// }
	// }
	// return buf.toString();
	// }
	//
	// public void outSrc(StringBuilder buf, String tab, String step) {
	// buf.append(getHtmlHead(tab, step) + getSrcBody(tab, step) +
	// getHtmlFoot(tab, step));
	// }

	// 引用CSS中Id的元素
	private static class Id extends AttrBase {
		public Id(CssLine cssLine) {
			super(cssLine.getName());
		}

		@Override
		public String getName() {
			return "id";
		}
	}

	// 引用CSS中Id的元素
	private static class Clazz extends AttrBase {
		public Clazz(String s) {
			super(s);
		}

		@Override
		public String getName() {
			return "class";
		}
	}

	/**
	 * Title: 锚<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static final class Anchor {
		private String _fileDir;
		private String _code;
		private String _name;

		public Anchor(String fileDir, String code, String name) {
			_fileDir = fileDir;
			_code = code;
			_name = name;
		}

		public String getUrl(Class callClazz) {
			return getUrl(FileTools.tranClassToFileDir(callClazz));
		}

		public String getUrl(String callDir) {
			int i = 0;
			char[] cc = callDir.toCharArray();
			char[] cf = _fileDir.toCharArray();
			int dir=0;
			for (; i < cc.length && i < cf.length; i++) {
				if(cc[i] == '\\' || cc[i] == '/')
					dir=i+1;
				if (cc[i] != cf[i])
					break;
			}
			int j = i=dir;
			StringBuilder buf = new StringBuilder();
			for (; i < cc.length; i++)
				if (cc[i] == '\\' || cc[i] == '/')
					buf.append(".." + Env.FILE_SEPARATOR);
			String file = buf.toString() + _fileDir.substring(j);
			if (file.length() == 0)
				return "#" + _code;
			if(_code.length()==0)
				return file+".htm";
			return file + ".htm#" + _code;
		}

		/**
		 * 取code
		 * 
		 * @return code
		 */
		public String getCode() {
			return _code;
		}

		/**
		 * 取name
		 * 
		 * @return name
		 */
		public String getName() {
			return _name;
		}

		/**
		 * @return the fileDir
		 */
		public String getFileDir() {
			return _fileDir;
		}
	}
}
