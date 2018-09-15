package irille.pub.html;


/**
 * 所有节点的类集合
 * 
 * @author whx
 * 
 */
public class Nodes extends AttributeCss {
	protected static final String LN = Base.LN;

	public static class A<T extends A> extends Base<T> {
		public T setHref(String href) {
			return set(new AttrCust("href", href));
		}

		public T setRel(String value) {
			return set("rel", value);
		}

		@Override
		public String getXmlLabel() {
			return "a";
		}

		@Override
		protected boolean[] getOutLnFlags() {
			return FALSE_TRUE;
		}
	}

	// <area> 标签定义图像映射中的区域（注：图像映射指得是带有可点击区域的图像）。
	// area 元素总是嵌套在 <map> 标签中。
	// 注释：<img> 标签中的 usemap 属性与 map 元素 name 属性相关联，创建图像与映射之间的联系。

	public static class Area<T extends Area> extends Base<T> {
		public Area() {
		}

		// 定义此区域的替换文本。
		public T setAlt(String text) {
			return set("alt", text);
		}

		// 定义此区域的目标 URL。
		public T setHref(String url) {
			return set(new AttrCust("href", url));
		}

		// 坐标值 定义可点击区域（对鼠标敏感的区域）的坐标。如："0,0,110,260"
		public T setCoords(String coords) {
			return set("coords", coords);
		}

		// 从图像映射排除某个区域。即指定区域没有连接
		public T setNohref() {
			return set("nohref", "nohref");
		}

		@Override
		public final boolean isClose() {
			return false;
		}

		@Override
		public String getXmlLabel() {
			return "area";
		}
	}

	// 呈现粗体文本效果
	public static class B<T extends B> extends NodeNotLn<T> {
		@Override
		public String getXmlLabel() {
			return "b";
		}
	}

	// 标签为页面上的所有链接规定默认地址或默认目标。
	public static class Base<T extends Base> extends Node<T> {
		// 规定在何处打开 href 属性指定的目标 URL。
		public T setTarget(String framename) {
			return set("target", tran(framename));
		}

		// 浏览器总在一个新打开、未命名的窗口中载入目标文档。
		public T setTargetBlank() {
			return setTarget("_blank");
		}

		// 这个目标的值对所有没有指定目标的 <a>
		// 标签是默认目标，它使得目标文档载入并显示在相同的框架或者窗口中作为源文档。这个目标是多余且不必要的，除非和文档标题 <base> 标签中的
		// target 属性一起使用。
		public T setTargetSelf() {
			return setTarget("_self");
		}

		// 这个目标使得文档载入父窗口或者包含来超链接引用的框架的框架集。如果这个引用是在窗口或者在顶级框架中，那么它与目标 _self 等效。
		public T setTargetParent() {
			return setTarget("_parent");
		}

		// 这个目标使得文档载入包含这个超链接的窗口，用 _top 目标将会清除所有被包含的框架并将文档载入整个浏览器窗口。
		public T setTargetTop() {
			return setTarget("_top");
		}

		@Override
		public String getXmlLabel() {
			return "base";
		}
	}

	public static class Big<T extends Big> extends NodeNotLn<T> {
		@Override
		public String getXmlLabel() {
			return "big";
		}
	}

	// 换行符
	public static class Br<T extends Br> extends NodeNotClose<T> {
		@Override
		public String getXmlLabel() {
			return "br";
		}
	}

	public static class Button<T extends Button> extends NodeNotLn<T> {
		// 规定禁用此按钮
		public T setDisabled() {
			return set("disabled", "disabled");
		}

		// 规定按钮的类型
		public T setTypeButton() {
			return set("type", "button");
		}

		public T setTypeReset() {
			return set("type", "reset");
		}

		public T setTypeSubmit() {
			return set("type", "submit");
		}

		@Override
		public String getXmlLabel() {
			return "button";
		}
	}

	// 指定表格标题
	public static class Caption<T extends Caption> extends Node<T> {
		public Caption(String string) {
			add(string);
		}

		@Override
		public String getXmlLabel() {
			return "caption";
		}
	}

	// <col> 标签为表格中一个或多个列定义属性值。
	// 如需对全部列应用样式，<col> 标签很有用，这样就不需要对各个单元和各行重复应用样式了。
	// 您只能在 table 或 colgroup 元素中使用 <col> 标签。
	public static class Col<T extends Col> extends Tbody<T> {

		// 规定 col 元素应该横跨的列数。
		public T setSpan(int span) {
			return set("span", "" + span);
		}

		// 规定 col 元素的宽度
		public T setWidth(int width) {
			return set("width", width + "px");
		}

		public T setWidth(String width) {
			return set("width", width);
		}

		@Override
		public String getXmlLabel() {
			return "col";
		}

	}

	public static class Dd<T extends Dd> extends Node<T> {

		@Override
		public String getXmlLabel() {
			return "dd";
		}
	}

	public static class Dir<T extends Dir> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "dir";
		}
	}

	public static class Div<T extends Div> extends Node<T> {
		public Div() {
			super();
		}

		public Div(CssLine... cssLines) {
			super();
			setClazz(cssLines);
		}
		
		@Override
		public String getXmlLabel() {
			return "div";
		}
	}

	// dl> 标签定义了定义列表。
	public static class Dl<T extends Dl> extends NodeNotLn<T> {
		@Override
		public String getXmlLabel() {
			return "dl";
		}
	}

	// <dt> 标签定义了定义列表中的项目（即术语部分）。
	public static class Dt<T extends Dt> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "dt";
		}
	}

	// fieldset 元素可将表单内的相关元素分组。
	// <fieldset> 标签将表单内容的一部分打包，生成一组相关表单的字段。
	// 当一组表单元素放到 <fieldset> 标签内时，浏览器会以特殊方式来显示它们，它们可能有特殊的边界、3D
	// 效果，或者甚至可创建一个子表单来处理这些元素。
	public static class Fieldset<T extends Fieldset> extends Node<T> {
		public Fieldset(String legendString) {
			if (legendString != null)
				add(new Legend(legendString));
		}

		@Override
		public String getXmlLabel() {
			return "fieldset";
		}
	}

	// <form> 标签用于为用户输入创建 HTML 表单。
	// 表单能够包含 input 元素，比如文本字段、复选框、单选框、提交按钮等等。
	// 表单还可以包含 menus、textarea、fieldset、legend 和 label 元素。
	// 表单用于向服务器传输数据。
	public static class Form<T extends Form> extends Base<T> {
		// 规定当提交表单时，向何处发送表单数据。
		public Form(String url) {
			set(new AttrCust("action", url));
		}

		// accept MIME_type 规定通过文件上传来提交的文件的类型。 STF
		// accept-charset charset 服务器处理表单数据所接受的字符集。 STF
		// enctype MIME_type 规定表单数据在发送到服务器之前应该如何编码。 STF

		// 规定如何发送表单数据。
		public Form setMethodGet() {
			return set("method", "get");
		}

		public Form setMethodPost() {
			return set("method", "post");
		}

		@Override
		public String getXmlLabel() {
			return "form";
		}
	}

	//
	// <frame> 标签定义 frameset 中的一个特定的窗口（框架）。
	public static class Frame<T extends Frame> extends NodeNotClose<T> {

		// 规定是否显示框架周围的边框。
		public T setFrameborder1() {
			return set("frameborder", "1");
		}

		public T setFrameborder0() {
			return set("frameborder", "0");
		}

		// 规定一个包含有关框架内容的长描述的页面。
		public T setLongdesc(String url) {
			return set(new AttrCust("longdesc", url));
		}

		// 定义框架的上方和下方的边距。
		public T setMarginheight(String size) {
			return set("marginheight", size);
		}

		// 定义框架的左侧和右侧的边距。
		public T setMarginwidth(String size) {
			return set("marginwidth", size);
		}

		// 规定无法调整框架的大小。
		public T setNoresize() {
			return set("noresize", "noresize");
		}

		// 规定是否在框架中显示滚动条。
		public T setScrollingYes() {
			return set("scrolling", "yes");
		}

		public T setScrollingNo() {
			return set("scrolling", "no");
		}

		public T setScrollingAuto() {
			return set("scrolling", "auto");
		}

		// 规定在框架中显示的文档的 URL。
		public T setSrc(String url) {
			return set(new AttrCust("src", url));
		}

		@Override
		public String getXmlLabel() {
			return "frame";
		}
	}

	public static class Frameset<T extends Frameset> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "frameset";
		}

	}

	public static class Noframes<T extends Noframes> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "noframes";
		}

	}

	//
	public static class H1<T extends H1> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "h1";
		}
	}

	//
	public static class H2<T extends H2> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "h2";
		}
	}

	//
	public static class H3<T extends H3> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "h3";
		}
	}

	//
	public static class H4<T extends H4> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "h4";
		}
	}

	//
	public static class H5<T extends H5> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "h5";
		}
	}

	//
	public static class H6<T extends H6> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "h6";
		}
	}

	// 换行符
	public static class Hr<T extends Hr> extends NodeNotClose<T> {
		@Override
		public String getXmlLabel() {
			return "hr";
		}
	}

	// 显示斜体文本效果。
	public static class I<T extends I> extends NodeNotLn<T> {
		@Override
		public String getXmlLabel() {
			return "i";
		}
	}

	public static class Img<T extends Img> extends NodeNotClose<T> {
		public static final int SCREEN_ACUTENESS = 96;

		public Img(String src, String alt) {
			set(new AttrCust("src", src));
			set("alt", alt);
		}

		public Img(String src) {
			set(new AttrCust("src", src));
		}

		@Override
		public String getXmlLabel() {
			return "img";
		}
	}

	public static class Em<T extends Em> extends Node<T> {

		public Em() {
		}

		public Em(String clazz) {
			setClazz(clazz);
		}

		@Override
		public String getXmlLabel() {
			return "em";
		}
	}

	// <input> 标签用于搜集用户信息。
	// 根据不同的 type 属性值，输入字段拥有很多种形式。输入字段可以是文本字段、复选框、掩码后的文本控件、单选按钮、按钮等等。

	public static class Input<T extends Input> extends NodeNotClose<T> {
		
		public Input() {
			super();
		}
		public Input(CssLine... cssLines) {
			super();
			setClazz(cssLines);
		}
		// 规定通过文件上传来提交的文件的类型。
		public T setAccept(String mimeType) {
			return set("accept", mimeType);
		}

		// 定义图像输入的替代文本。
		public T setAlt(String text) {
			return set("alt", text);
		}

		// 规定此 input 元素首次加载时应当被选中。
		public T setChecked() {
			return set("checked", "checked");
		}

		// 当 input 元素加载时禁用此元素。
		public T setDisabled() {
			return set("disabled", "disabled");
		}

		// 规定输入字段中的字符的最大长度。
		public T setMaxlength(int maxlength) {
			return set("maxlength", maxlength + "");
		}

		// 规定输入字段为只读。
		public T setReadonly() {
			return set("readonly", "readonly");
		}

		// 定义输入字段的宽度。
		public T setSize(int numberOfChar) {
			return set("size", numberOfChar + "");
		}

		// 定义以提交按钮形式显示的图像的 URL。
		public T setSrc(String src) {
			return set(new AttrCust("src", src));
		}

		public T setTypeButton() {
			return set("type", "button");
		}

		public T setTypeCheckbox() {
			return set("type", "checkbox");
		}

		public T setTypeFile() {
			return set("type", "file");
		}

		public T setTypeHidden() {
			return set("type", "hidden");
		}

		public T setTypeImage() {
			return set("type", "image");
		}

		public T setTypePassword() {
			return set("type", "password");
		}

		public T setTypeRadio() {
			return set("type", "radio");
		}

		public T setTypeReset() {
			return set("type", "reset");
		}

		public T setTypeSubmit() {
			return set("type", "submit");
		}

		public T setTypeText() {
			return set("type", "text");
		}

		// 规定 input 元素的值。
		public T setValue(String value) {
			return set("value", value);
		}

		@Override
		public String getXmlLabel() {
			return "input";
		}
	}

	// <label> 标签为 input 元素定义标注（标记）。
	public static class Label<T extends Label> extends NodeNotLn<T> {
		// 规定 label 与哪个表单元素绑定。
		public Label(String forId, String labelString) {
			set("for", forId);
			add(labelString);
		}
		// 规定 label 与哪个表单元素绑定。
		public Label(String labelString) {
			add(labelString);
		}

		@Override
		public String getXmlLabel() {
			return "label";
		}
	}

	// legend 元素为 fieldset 元素定义标题（caption）。
	public static class Legend<T extends Legend> extends Node<T> {
		public Legend(String text) {
			add(text);
		}

		@Override
		public String getXmlLabel() {
			return "legend";
		}
	}

	// <li> 标签定义列表项目。
	// <li> 标签可用在有序列表 (<ol>) 和无序列表 (<ul>) 中。
	public static class Li<T extends Li> extends NodeNotLn<T> {

		@Override
		public String getXmlLabel() {
			return "li";
		}
	}

	// 定义一个客户端图像映射。图像映射（image-map）指带有可点击区域的一幅图像。
	public static class Map<T extends Map> extends Node<T> {
		public Map(String id) {
			setId(id);
		}

		@Override
		public String getXmlLabel() {
			return "map";
		}
	}

	// <menu> 标签可定义一个菜单列表。
	public static class Menu<T extends Menu> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "menu";
		}
	}

	// 定义一个嵌入的对象。请使用此元素向您的 XHTML 页面添加多媒体。此元素允许您规定插入 HTML
	// 文档中的对象的数据和参数，以及可用来显示和操作数据的代码。
	// <object> 标签用于包含对象，比如图像、音频、视频、Java applets、ActiveX、PDF 以及 Flash。
	public static class ObjectH<T extends ObjectH> extends Node<T> {

		// 规定与 OBject 元素相关的内容的水平对齐方式
		public T setAlignRight() {
			return set("align", "right");
		}

		public T setAlignLeft() {
			return set("align", "left");
		}

		public T setAlignTop() {
			return set("align", "top");
		}

		public T setAlignBottom() {
			return set("align", "bottom");
		}

		// 由空格分隔的指向档案文件的 URL 列表。这些档案文件包含了与对象相关的资源。
		public T setArchive(String url) {
			return set(new AttrCust("archive", url));
		}

		// 定义对象周围的边框。
		public T setBorder(String border) {
			return set("border", border);
		}

		// 定义嵌入 Windows Registry 中或某个 URL 中的类的 ID 值，此属性可用来指定浏览器中包含的对象的位置，通常是一个 Java
		// 类。
		public T setClassid(String classId) {
			return set("classid", classId);
		}

		// 通过 classid 属性所引用的代码的 MIME 类型。
		public T setCodetype(String mimeType) {
			return set("codetype", mimeType);
		}

		// 定义在何处可找到对象所需的代码，提供一个基准 URL。
		public T setCodebase(String url) {
			return set(new AttrCust("codebase", url));
		}

		// 定义引用对象数据的 URL。如果有需要对象处理的数据文件,要用 data 属性来指定这些数据文件。
		public T setData(String url) {
			return set(new AttrCust("data", url));
		}

		// 可定义此对象仅可被声明，但不能被创建或例示，直到此对象得到应用为止。
		public T setSeclare() {
			return set("declare", "declare");
		}

		// 定义对象的高度。
		public T setHeight(String height) {
			return set("height", height);
		}

		// 定义对象周围水平方向的空白。
		public T setHspace(String hspace) {
			return set("hspace", hspace);
		}

		// 定义当对象正在加载时所显示的文本。
		public T setStandby(String standbyText) {
			return set("standby", standbyText);
		}

		// 定义被规定在 data 属性中指定的文件中出现的数据的 MIME 类型。
		public T setType(String mimeType) {
			return set("type", mimeType);
		}

		// 规定与对象一同使用的客户端图像映射的 URL。
		public T setUsemap(String url) {
			return set(new AttrCust("usemap", url));
		}

		// 定义对象的垂直方向的空白。
		public T setVspace(String pixels) {
			return set("vspace", pixels);
		}

		// 定义对象的宽度。
		public T setWidth(String pixels) {
			return set("width", pixels);
		}

		@Override
		public String getXmlLabel() {
			return "object";
		}
	}

	// <ol> 标签定义有序列表。
	public static class Ol<T extends Ol> extends Node<T> {

		@Override
		public String getXmlLabel() {
			return "ol";
		}
	}

	// <optgroup> 标签定义选项组。
	// optgroup 元素用于组合选项。当您使用一个长的选项列表时，对相关的选项进行组合会使处理更加容易。
	public static class Optgroup<T extends Optgroup> extends Node<T> {
		public Optgroup(String labelText) {
			set("label", labelText);
		}

		// disabled disabled 规定禁用该选项组。
		public T setDisabled() {
			return set("disabled", "disabled");
		}

		@Override
		public String getXmlLabel() {
			return "optgroup";
		}
	}

	// option 元素定义下拉列表中的一个选项（一个条目）。
	// 浏览器将 <option> 标签中的内容作为 <select> 标签的菜单或是滚动列表中的一个元素显示。
	// option 元素位于 select 元素内部。

	public static class Option<T extends Option> extends Node<T> {

		// disabled disabled 规定禁用该选项组。
		public T setDisabled() {
			return set("disabled", "disabled");
		}

		// 规定选项（在首次显示在列表中时）表现为选中状态。
		public T setSelected() {
			return set("selected", "selected");
		}

		// 定义送往服务器的选项值。
		public T setValue(String value) {
			return set("value", value);
		}

		// 定义当使用 <optgroup> 时所使用的标注。
		public T setLabel(String label) {
			return set("label", label);
		}

		@Override
		public String getXmlLabel() {
			return "option";
		}
	}

	// param 元素允许您为插入 XHTML 文档的对象规定 run-time 设置，也就是说，此标签可为包含它的 <object> 或者
	// <applet> 标签提供参数。
	public static class Param<T extends Param> extends Node<T> {
		public Param(String name) {
			setName(name);
		}

		public T setValuetypeData() {
			return set("valuetype", "data");
		}

		public T setValuetypeRef() {
			return set("valuetype", "ref");
		}

		public T setValuetypeObject() {
			return set("valuetype", "object");
		}

		// 规定参数的 MIME 类型（internet media type）
		public T setType(String mimeType) {
			return set("type", mimeType);
		}

		// 规定参数的值。
		public T setValue(String value) {
			return set("value", value);
		}

		@Override
		public String getXmlLabel() {
			return "param";
		}
	}

	// pre 元素可定义预格式化的文本。被包围在 pre 元素中的文本通常会保留空格和换行符。而文本也会呈现为等宽字体。
	// <pre> 标签的一个常见应用就是用来表示计算机的源代码。
	public static class Pre<T extends Pre> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "pre";
		}
	}

	// <q> 标签定义短的引用。
	// 浏览器经常在引用的内容周围添加引号。
	public static class Q<T extends Q> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "q";
		}
	}

	// <script> 标签用于定义客户端脚本，比如 JavaScript。
	public static class Script<T extends Script> extends Node<T> {

		public T setTypeJavaScript() {
			return set("type", JS);
		}

		// 规定是否对脚本执行进行延迟，直到页面加载为止。
		public T setDefer() {
			return set("defer", "defer");
		}

		// 规定外部脚本文件的 URL。
		public T setSrc(String src) {
			return set(new AttrCust("src", src));
		}

		// xml:space preserve 规定是否保留代码中的空白。

		@Override
		public String getXmlLabel() {
			return "script";
		}
	}

	// select 元素可创建单选或多选菜单。
	// 当提交表单时，浏览器会提交选定的项目，或者收集用逗号分隔的多个选项，将其合成一个单独的参数列表，并且在将 <select> 表单数据提交给服务器时包括
	// name 属性。
	public static class Select<T extends Select> extends Node<T> {
		// 规定下拉列表中可见选项的数目。
		public T setSize(int number) {
			return set("size", number + "");
		}

		// 规定可选择多个选项
		public T setMultiple() {
			return set("multiple", "multiple");
		}

		// disabled disabled 规定禁用该选项组。
		public T setDisabled() {
			return set("disabled", "disabled");
		}

		@Override
		public String getXmlLabel() {
			return "select";
		}
	}

	public static class Small<T extends Small> extends NodeNotLn<T> {
		@Override
		public String getXmlLabel() {
			return "small";
		}
	}

	// <span> 标签被用来组合文档中的行内元素。
	public static class Span<T extends Span> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "span";
		}

		@Override
		protected boolean[] getOutLnFlags() {
			return FALSE_TRUE;
		}
	}

	// <span> 标签被用来组合文档中的行内元素。
	public static class StyleNode<T extends StyleNode> extends Node<T> {
		private Css _css;
		private String _rem;

		public StyleNode(String rem, Css css) {
			super();
			_css = css;
			_rem = rem;
		}

		@Override
		protected void createXmlBody(IXmlBuf buf, TabStruct tab) {
			buf.appendStr(new CssLine(null, CssLine.REM + _rem).outCssCode(tab.add()
			    .getTab()));
			buf.appendStr(_css.outCssCode(tab.getTab()));
			tab.sub();
		}

		@Override
		public String getXmlLabel() {
			return "style";
		}
	}

	// <sub> 标签可定义下标文本。
	// 包含在 <sub> 标签和其结束标签 </sub> 中的内容将会以当前文本流中字符高度的一半来显示，但是与当前文本流中文字的字体和字号都是一样的。
	public static class Sub<T extends Sub> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "sub";
		}
	}

	// <sup> 标签可定义上标文本。
	public static class Sup<T extends Sup> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "sup";
		}
	}

	// <table> 标签定义 HTML 表格。
	// 简单的 HTML 表格由 table 元素以及一个或多个 tr、th 或 td 元素组成。
	// tr 元素定义表格行，th 元素定义表头，td 元素定义表格单元。
	// 更复杂的 HTML 表格也可能包括 caption、col、colgroup、thead、tfoot 以及 tbody 元素。
	public static class Table<T extends Table> extends Node<T> {
		// 增加表头, 每个对象自动为 <Th>标签括起来
		public T addTh(Object... objs) {
			Tr tr = new Tr();
			add(tr);
			for (Object obj : objs) {
				if (obj instanceof Th)
					tr.add((Th) obj);
				else if (obj instanceof Node)
					tr.add(new Th()).add((Node) obj);
				else
					tr.add(new Th()).add(obj.toString());
			}
			return (T) this;
		}

		// 增加表体行, 每个对象自动为 <Th>标签括起来
		public T addTd(Object... objs) {
			Td tr = new Tr();
			add(tr);
			for (Object obj : objs) {
				if (obj instanceof Td)
					tr.add((Td) obj);
				else if (obj instanceof Node)
					tr.add(new Td()).add((Node) obj);
				else
					tr.add(new Td()).add(obj.toString());
			}
			return (T) this;
		}

		public Tr addTr() {
			Tr tr = new Tr();
			add(tr);
			return tr;
		}

		// 规定表格边框的宽度。
		public T setBorder(String pixels) {
			return set("border", pixels);
		}

		// 规定单元边沿与其内容之间的空白。
		public T setCellpadding(String pixels) {
			return set("cellpadding", pixels);
		}

		// 规定单元格之间的空白。
		public T setSellspacing(String pixels) {
			return set("cellspacing", pixels);
		}

		// 规定外侧边框的哪个部分是可见的。
		public T setFrameVoid() {
			return set("frame", "void");
		}

		public T setFrameAbove() {
			return set("frame", "above");
		}

		public T setFramBelow() {
			return set("frame", "below");
		}

		public T setFrameHsides() {
			return set("frame", "hsides");
		}

		public T setFrameLhs() {
			return set("frame", "lhs");
		}

		public T setFrameRhs() {
			return set("frame", "rhs");
		}

		public T setFrameVsides() {
			return set("frame", "vsides");
		}

		public T setFrameBox() {
			return set("frame", "box");
		}

		public T setFrameBorder() {
			return set("frame", "border");
		}

		// 规定内侧边框的哪个部分是可见的。
		public T setRulesNone() {
			return set("rules", "none");
		}

		public T setRulesGroups() {
			return set("rules", "groups");
		}

		public T setRulesRows() {
			return set("rules", "rows");
		}

		public T setRulesCols() {
			return set("rules", "cols");
		}

		public T setRulesAll() {
			return set("rules", "all");
		}

		public T setSummary(String text) {
			return set("summary", text);
		}

		// 规定表格的宽度。
		public T setWidth(String width) {
			return set("width", width);
		}

		@Override
		public String getXmlLabel() {
			return "table";
		}
	}

	public static class Tbody<T extends Tbody> extends Node<T> {
		// 规定根据哪个字符来对齐与 col 元素相关的内容。
		public T setChar(char c) {
			return set("char", "" + c);
		}

		// 规定第一个对齐字符的偏移量。
		public T setCharoff(int number) {
			return set("charoff", "" + number);
		}

		// 定义与 col 元素相关的内容的垂直对齐方式
		public T setValignTop() {
			return set("valign", "top");
		}

		public T setValignMiddle() {
			return set("valign", "middle");
		}

		public T setValignBottom() {
			return set("valign", "bottom");
		}

		public T setValignBaseline() {
			return set("valign", "baseline");
		}

		// 规定与 col 元素相关的内容的水平对齐方式
		public T setAlignRight() {
			return set("align", "right");
		}

		public T setAlignLeft() {
			return set("align", "left");
		}

		public T setAlignCenter() {
			return set("align", "center");
		}

		public T setAlignJustify() {
			return set("align", "justify");
		}

		public T setAlignChar() {
			return set("align", "char");
		}

		@Override
		public String getXmlLabel() {
			return "tbody";
		}
	}

	// <td> 标签定义 HTML 表格中的标准单元格。
	// HTML 表格有两类单元格：
	// 表头单元 - 包含头部信息（由 th 元素创建）
	// 标准单元 - 包含数据（由 td 元素创建）
	// td 元素中的文本一般显示为正常字体且左对齐。
	public static class Td<T extends Td> extends Tbody<T> {
		// 规定单元格中内容的缩写版本。
		public T setAbbr(String text) {
			return set("abbr", text);
		}

		// 对单元进行分类。
		public T setAxis(String categoryName) {
			return set("axis", categoryName);
		}

		// 规定表格的宽度。
		public T setWidth(String width) {
			return set("width", width);
		}

		public T setWidth(int px) {
			return set("width", px + "px");
		}

		// 规定单元格可横跨的列数。
		public T setColspan(int number) {
			return set("colspan", number + "");
		}

		// 规定与单元格相关的表头。
		public T setHeaders(String headerCellsId) {
			return set("headers", headerCellsId);
		}

		// 规定单元格可横跨的行数。
		public T setRowspan(int number) {
			return set("rowspan", "" + number);
		}

		// 定义将表头数据与单元数据相关联的方法。
		public T setScopeCol() {
			return set("scope", "col");
		}

		public T setScopeColgroup() {
			return set("scope", "colgroup");
		}

		public T setScopeRow() {
			return set("scope", "row");
		}

		public T setScopeRowgroup() {
			return set("scope", "rowgroup");
		}

		@Override
		public String getXmlLabel() {
			return "td";
		}

		@Override
		protected boolean[] getOutLnFlags() {
			return FALSE_TRUE;
		}
	}

	// <textarea> 标签定义多行的文本输入控件。
	public static class Textarea<T extends Textarea> extends NodeNotLn<T> {
		// cols number 规定文本区内的可见宽度。 STF
		// rows number 规定文本区内的可见行数。
		public Textarea(int cols, int rows) {
			set("cols", "" + cols);
			set("rows", "" + rows);
		}

		public Textarea() {
		}

		// 规定文本区为只读。
		public T setReadonly() {
			return set("readonly", "readonly");
		}

		// disabled disabled 规定禁用该选项组。
		public T setDisabled() {
			return set("disabled", "disabled");
		}

		@Override
		public String getXmlLabel() {
			return "textarea";
		}
	}

	public static class Tfoot<T extends Tfoot> extends Tbody<T> {
		@Override
		public String getXmlLabel() {
			return "tfoot";
		}
	}

	// 定义表格内的表头单元格。
	public static class Title<T extends Title> extends NodeNotLn<T> {
		@Override
		public String getXmlLabel() {
			return "title";
		}
	}

	// 定义表格内的表头单元格。
	public static class Th<T extends Th> extends Td<T> {
		@Override
		public String getXmlLabel() {
			return "th";
		}
	}

	// <thead> 标签定义表格的表头。该标签用于组合 HTML 表格的表头内容。
	public static class Thead<T extends Thead> extends Tbody<T> {
		@Override
		public String getXmlLabel() {
			return "thead";
		}
	}

	// 定义表格内的表头单元格。
	public static class Tr<T extends Tr> extends Td<T> {
		@Override
		public String getXmlLabel() {
			return "tr";
		}

		public Th addTh() {
			Th th = new Th();
			add(th);
			return th;
		}

		public Td addTd() {
			Td td = new Td();
			add(td);
			return td;
		}

		@Override
		protected boolean[] getOutLnFlags() {
			return TRUE_TRUE;
		}
	}

	// 呈现类似打字机或者等宽的文本效果。
	public static class Tt<T extends Tt> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "tt";
		}
	}

	// <u> 标签可定义下划线文本。
	public static class U<T extends U> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "u";
		}
	}

	public static class Ul<T extends Ul> extends Node<T> {
		@Override
		public String getXmlLabel() {
			return "ul";
		}
	}

	public static class CssNode<T extends CssNode> extends Node<T> {
		private CssLine _cssLine;

		public CssNode(CssLine cssLine) {
			_cssLine = cssLine;
			cssLine.assertElelement(); // 必须是Css的普通的元素，不可以是Id或Class
		}

		@Override
		public String getXmlLabel() {
			return _cssLine.getName();
		}
	}

	// 换行
	public static class P<T extends P> extends NodeNotLn<T> {
		@Override
		public String getXmlLabel() {
			return "P";
		}
	}

	/**
	 * 没有标签的节点，功能与Span类似，用于包含一些子节点
	 * 
	 * @author whx
	 * 
	 * @param <T>
	 */
	public static class Temp<T extends Temp> extends NodeNotLn<T> {
		@Override
		protected void createXmlHead(IXmlBuf buf, TabStruct tab) {
		}

		@Override
		protected void createXmlFoot(IXmlBuf buf, TabStruct tab) {
		}

		@Override
		public String getXmlLabel() {
			return "";
		}
	}

	// 空白标签的基类
	protected abstract static class NodeNotClose<T extends NodeNotClose> extends
	    Node<T> {
		@Override
		public final boolean isClose() {
			return false;
		}
	}

	// 换行的标签基类
	protected abstract static class NodeNotLn<T extends NodeNotLn> extends
	    Node<T> {
		@Override
		protected boolean[] getOutLnFlags() {
			return FALSE_TRUE;
		}
	}

	protected static class NodeCust<T extends NodeCust> extends Node<T> {
		String _xmlLabel;
		boolean _isClose;

		public NodeCust(String xmlLabel, boolean isClose) {
			_xmlLabel = xmlLabel;
			_isClose = isClose;
		}

		public NodeCust(String xmlLabel) {
			_xmlLabel = xmlLabel;
			_isClose = true;
		}

		@Override
		public final boolean isClose() {
			return _isClose;
		}

		@Override
		public String getXmlLabel() {
			return _xmlLabel;
		}
	}
}
