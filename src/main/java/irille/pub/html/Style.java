package irille.pub.html;

import irille.pub.IPubVars;
import irille.pub.html.AttributeCss.Backgroud;
import irille.pub.html.AttributeCss.BackgroudAttachment;
import irille.pub.html.AttributeCss.BackgroudColor;
import irille.pub.html.AttributeCss.BackgroudPosition;
import irille.pub.html.AttributeCss.BackgroudRepeat;
import irille.pub.html.AttributeCss.Border;
import irille.pub.html.AttributeCss.BorderBottom;
import irille.pub.html.AttributeCss.BorderCollapse;
import irille.pub.html.AttributeCss.BorderColor;
import irille.pub.html.AttributeCss.BorderLeft;
import irille.pub.html.AttributeCss.BorderRight;
import irille.pub.html.AttributeCss.BorderStyle;
import irille.pub.html.AttributeCss.BorderTop;
import irille.pub.html.AttributeCss.BorderWidth;
import irille.pub.html.AttributeCss.Bottom;
import irille.pub.html.AttributeCss.Clear;
import irille.pub.html.AttributeCss.Clip;
import irille.pub.html.AttributeCss.Cursor;
import irille.pub.html.AttributeCss.Display;
import irille.pub.html.AttributeCss.Filter;
import irille.pub.html.AttributeCss.Float_;
import irille.pub.html.AttributeCss.FontFamily;
import irille.pub.html.AttributeCss.FontSize;
import irille.pub.html.AttributeCss.FontStyle;
import irille.pub.html.AttributeCss.FontWeight;
import irille.pub.html.AttributeCss.Height;
import irille.pub.html.AttributeCss.Left;
import irille.pub.html.AttributeCss.LetterSpacing;
import irille.pub.html.AttributeCss.LineHeight;
import irille.pub.html.AttributeCss.ListStylePosition;
import irille.pub.html.AttributeCss.ListStyleType;
import irille.pub.html.AttributeCss.Margin;
import irille.pub.html.AttributeCss.MarginBottom;
import irille.pub.html.AttributeCss.MarginLeft;
import irille.pub.html.AttributeCss.MarginRight;
import irille.pub.html.AttributeCss.MarginTop;
import irille.pub.html.AttributeCss.MaxHeight;
import irille.pub.html.AttributeCss.MaxWidth;
import irille.pub.html.AttributeCss.MinHeight;
import irille.pub.html.AttributeCss.MinWidth;
import irille.pub.html.AttributeCss.Overflow;
import irille.pub.html.AttributeCss.Padding;
import irille.pub.html.AttributeCss.PaddingBottom;
import irille.pub.html.AttributeCss.PaddingLeft;
import irille.pub.html.AttributeCss.PaddingRight;
import irille.pub.html.AttributeCss.PaddingTop;
import irille.pub.html.AttributeCss.Position;
import irille.pub.html.AttributeCss.Right;
import irille.pub.html.AttributeCss.TableLayout;
import irille.pub.html.AttributeCss.TextAlign;
import irille.pub.html.AttributeCss.TextDecoration;
import irille.pub.html.AttributeCss.TextIndent;
import irille.pub.html.AttributeCss.Top;
import irille.pub.html.AttributeCss.VerticalAlign;
import irille.pub.html.AttributeCss.Visibility;
import irille.pub.html.AttributeCss.Width;
import irille.pub.html.AttributeCss.ZIndex;
import irille.pub.html.AttributeCss._Display;
import irille.pub.html.AttributeCss._Margin;
import irille.pub.html.Css.Color;
import irille.pub.html.ImgMerge.PostSize;

import java.util.Vector;


/**
 * 样式类，在Html代码中标签属性中的style="属性列表" 时用到，CssLines类也继承自此类
 * @author whx
 *
 * @param <T>
 */
public class Style<T extends Style>  implements IPubVars{
	protected Vector<AttrBase> _as = null;

	public Style(AttrBase... as) {
		add(as);
	}

	public final T _() {
		_as.get(_as.size()-1)._();
		return (T)this;
	}
	public final T add(AttrBase... as) {
		if (_as == null)
			_as = new Vector<AttrBase>();
		for (AttrBase a : as)
			_as.add(a);
		return (T) this;
	}
	
	public String getName() {
		return "style";
	}

	public String getAttributes() {
		StringBuilder buf = new StringBuilder();
		for (AttrBase a : _as)
			buf.append(a.outCssCode());
		return buf.toString();
	}
	
	
	public T Backgroud(String str) {
		return add(new Backgroud(str));
	}
	public T Backgroud(String urlFile,String str) {
		return add(new Backgroud("url("+urlFile+") "+str));
	}
	public T BackgroudNoRepeat(String urlFile) {
		return add(new Backgroud("url("+urlFile+") no-repeat"));
	}
	/**
	 * 产生大图片文件的背景属性代码，如：background-image:url(/整图地址); background-repeat:no-repeat
	 * @param merge 合并的大图片对象
	 * @return 当前对象
	 */
	public T BackgroudMerge(ImgMerge merge) {
		return add(new Backgroud("url("+merge.getOutFile()+") no-repeat"));
	}
	/**
	 * 	产生小图片的属性值代码，如：width:容器大小;height:容器高度;background-position:X坐标 Y坐标
	 * @param merge 合并的大图片对象
	 * @param imgFileName 小图片文件名
	 * @return 当前对象
	 */
	public T ImgMerge( ImgMerge merge, String imgFileName) {
		PostSize ps=merge.getPostSize(imgFileName);
		Width(ps.getWidth());
		Height(ps.getHeight());
		return BackgroudPosition("0 "+ ps.getY());
	}
	
	public T BackgroudAttachmentFixed() {
		return add(new BackgroudAttachment("fixed"));
	}

	public T BackgroudAttachmentScrool() {
		return add(new BackgroudAttachment("scroll"));
	}

	public T BackgroudColor(String color) {
		return add(new BackgroudColor(color));
	}
	public T BackgroudPosition(int px) {
		return add(new BackgroudPosition(px+"px"));
	}
	public T BackgroudPosition(String size) {
		return add(new BackgroudPosition(size));
	}
	public T BackgroudPositionLeft() {
		return add(new BackgroudPosition("left"));
	}

	public T BackgroudPositionLeftTop() {
		return add(new BackgroudPosition("left top"));
	}

	public T BackgroudPositionLeftBottom() {
		return add(new BackgroudPosition("left bottom"));
	}

	public T BackgroudPositionRight() {
		return add(new BackgroudPosition("right"));
	}

	public T BackgroudPositionRightTop() {
		return add(new BackgroudPosition("right top"));
	}

	public T BackgroudPositionRightBottom() {
		return add(new BackgroudPosition("right bottom"));
	}

	public T BackgroudPositionTop() {
		return add(new BackgroudPosition("top"));
	}

	public T BackgroudPositionBottom() {
		return add(new BackgroudPosition("bottom"));
	}

	public T BackgroudPositionCenter() {
		return add(new BackgroudPosition("center"));
	}

	public T BackgroudRepeatX() {
		return add(new BackgroudRepeat("repeat-x"));
	}

	public T BackgroudRepeatY() {
		return add(new BackgroudRepeat("repeat-y"));
	}

	public T BackgroudRepeatNo() {
		return add(new BackgroudRepeat("no repeat"));
	}
	public T Border(String str) {
		return add(new Border(str));
	}
	public T BorderLeft(String str) {
		return add(new BorderLeft(str));
	}
	public T BorderRight(String str) {
		return add(new BorderRight(str));
	}
	public T BorderTop(String str) {
		return add(new BorderTop(str));
	}
	public T BorderBottom(String str) {
		return add(new BorderBottom(str));
	}

	/**
	 * 默认值。边框会被分开。不会忽略 border-spacing 和 empty-cells 属性。
	 * 
	 * @return 本对象
	 */
	public T BorderCollapseSeparate() {
		return add(new BorderCollapse("separate"));
	}

	/**
	 * 如果可能，边框会合并为一个单一的边框。会忽略 border-spacing 和 empty-cells 属性。
	 * 
	 * @return 本对象
	 */
	public T BorderCollapseCollapse() {
		return add(new BorderCollapse("collapse"));
	}

	public T BorderColor(String color) {
		return add(new BorderColor(color));
	}

	public T BorderStyleNone() {
		return add(new BorderStyle("none"));
	}

	public T BorderStyleSolid() {
		return add(new BorderStyle("solid"));
	}

	public T BorderStyleDouble() {
		return add(new BorderStyle("double"));
	}

	public T BorderWidth(String size) {
		return add(new BorderWidth(size));
	}
	public T BorderWidth(int px) {
		return add(new BorderWidth(px));
	}

	public T Bottom(String size) {
		return add(new Bottom(size));
	}
	public T Bottom(int px) {
		return add(new Bottom(px));
	}

	public T ClearNone() {
		return add(new Clear("none"));
	}

	public T ClearLeft() {
		return add(new Clear("left"));
	}

	public T ClearRight() {
		return add(new Clear("right"));
	}

	public T ClearBoth() {
		return add(new Clear("both"));
	}

	public T Clip(int top, int right, int bottom, int left) {
		return add(new Clip(top, right, bottom, left));
	}
	public T Clip(String val) {
		return add(new Clip(val));
	}

	public T Color(String color) {
		return add(new Color(color));
	}
	/**
	 * 光标呈现为十字线
	 */
	public final T CursorCorsshair() {
		return add(new Cursor("crosshair"));		
	}
	/**
	 * 光标呈现为指示链接的指针（一只手）
	 * @return
	 */
	public final T CursorPointer() {
		return add(new Cursor("pointer"));		
	}
	/**
	 * 此光标指示某对象可被移动。
	 * @return
	 */
	public final T CursorMove() {
		return add(new Cursor("move"));		
	}
	/**
	 * 默认光标（通常是一个箭头）
	 * @return
	 */
	public final T CursorDefault() {
		return add(new Cursor("default"));		
	}
	/**
	 * 此光标指示文本。
	 * @return
	 */
	public final T CursorText() {
		return add(new Cursor("text"));		
	}
	/**
	 * 此光标指示程序正忙（通常是一只表或沙漏）。
	 * @return
	 */
	public final T CursorWait() {
		return add(new Cursor("wait"));		
	}
	/**
	 * 此光标指示可用的帮助（通常是一个问号或一个气球）。
	 * @return
	 */
	public final T CursorHelp() {
		return add(new Cursor("help"));		
	}

	public T Cust(String name, String value) {
		return add(new AttrCust(name, value));
	}
	/**
	 * 	inherit 规定应该从父元素继承 display 属性的值。 
	 * @return
	 */
	public T _DisplayInherit() {
		return add(new _Display("inherit"));
	}
	/**
	 * 	none 此元素不会被显示
	 * @return
	 */
	public T _DisplayNone() {
		return add(new _Display("none"));
	}
	/**
	 * 	block 此元素将显示为块级元素，此元素前后会带有换行符。 
	 * @return
	 */
	public T _DisplayBlock() {
		return add(new _Display("block"));
	}

	
	/**
	 * 	inline 默认。此元素会被显示为内联元素，元素前后没有换行符。 
	 * @return
	 */
	public T _DisplayInline() {
		return add(new _Display("inline"));
	}
	/**
	 * inline-block 行内块元素。（CSS2.1 新增的值） 	
	 * @return
	 */
	public T _DisplayInlineBlock() {
		return add(new _Display("inline-block"));
	}
	
	/**
	 * 	inherit 规定应该从父元素继承 display 属性的值。 
	 * @return
	 */
	public T DisplayInherit() {
		return add(new Display("inherit"));
	}
	/**
	 * 	none 此元素不会被显示
	 * @return
	 */
	public T DisplayNone() {
		return add(new Display("none"));
	}
	/**
	 * 	block 此元素将显示为块级元素，此元素前后会带有换行符。 
	 * @return
	 */
	public T DisplayBlock() {
		return add(new Display("block"));
	}

	
	/**
	 * 	inline 默认。此元素会被显示为内联元素，元素前后没有换行符。 
	 * @return
	 */
	public T DisplayInline() {
		return add(new Display("inline"));
	}
	/**
	 * inline-block 行内块元素。（CSS2.1 新增的值） 	
	 * @return
	 */
	public T DisplayInlineBlock() {
		return add(new Display("inline-block"));
	}
	
	
	/**
	 * 	run-in 此元素会根据上下文作为块级元素或内联元素显示。 
	 * @return
	 */
	public T DisplayRunIn() {
		return add(new Display("run-in"));
	}
	/**
	 * 	list-item 此元素会作为列表显示。 
	 * @return
	 */
	public T DisplayListItem() {
		return add(new Display("list-item"));
	}

	public T Filter(String str) {
		return add(new Filter(str));
	}

	public T FloatLeft() {
		return add(new Float_("left"));
	}

	public T FloatRight() {
		return add(new Float_("right"));
	}

	public T FloatNone() {
		return add(new Float_("none"));
	}

	public T FontFamily(String font) {
		return add(new FontFamily(font));
	}

	public T FontSize(String size) {
		return add(new FontSize(size));
	}
	public T FontSize(int px) {
		return add(new FontSize(px));
	}

	public T FontStyleNormal() {
		return add(new FontStyle("normal"));
	}

	public T FontStyleItalic() {
		return add(new FontStyle("italic"));
	}

	public T FontWeightNormal() {
		return add(new FontWeight("normal"));
	}
	public T FontWeightBold() {
		return add(new FontWeight("bold"));
	}
	public T FontWeightBolder() {
		return add(new FontWeight("bolder"));
	}
	public T Height(String size) {
		return add(new Height(size));
	}
	
	public T Height(int px) {
		return add(new Height(px));
		
	}

	public T Left(String size) {
		return add(new Left(size));
	}
	public T Left(int px) {
		return add(new Left(px));
	}

	public T LetterSpacing(String size) {
		return add(new LetterSpacing(size));
	}
	public T LetterSpacing(int px) {
		return add(new LetterSpacing(px+"px"));
	}

	public T LineHeight(String size) {
		return add(new LineHeight(size));
	}
	public T LineHeight(int px) {
		return add(new LineHeight(px));
	}

	/**
	 * 列表项目标记放置在文本以内，且环绕文本根据标记对齐。
	 * 
	 * @return
	 */
	public T ListStylePositionInside() {
		return add(new ListStylePosition("inside"));
	}

	/**
	 * 默认值。保持标记位于文本的左侧。列表项目标记放置在文本以外，且环绕文本不根据标记对齐。
	 * 
	 * @return
	 */
	public T ListStylePositionOutside() {
		return add(new ListStylePosition("outside"));
	}

	/**
	 * 无标记
	 * 
	 * @return
	 */
	public T ListStyleTypeNone() {
		return add(new ListStyleType("none"));
	}

	/**
	 * 默认。标记是实心圆。
	 */
	public T ListStyleTypeDisc() {
		return add(new ListStyleType("disc"));
	}

	/**
	 * 标记是空心圆。
	 * 
	 * @return
	 */
	public T ListStyleTypeCircle() {
		return add(new ListStyleType("circle"));
	}

	/**
	 * 标记是实心方块。
	 * 
	 * @return
	 */
	public T ListStyleTypeSquare() {
		return add(new ListStyleType("square"));
	}

	/**
	 * 标记是数字。
	 * 
	 * @return
	 */
	public T ListStyleTypeDecimal() {
		return add(new ListStyleType("decimal"));
	}

	public T Margin(String size) {
		return add(new Margin(size));
	}
	public T Margin(int px) {
		return add(new Margin(px+"px"));
	}
	public T Margin(int topPx,int rightPx,int bottomPx,int leftPx) {
		return add(new Margin(topPx+"px "+rightPx+"px "+bottomPx+"px "+leftPx+"px"));
	}
	public T _Margin(int px) {
		return add(new _Margin(px+"px"));
	}
	public T _Margin(int topPx,int rightPx,int bottomPx,int leftPx) {
		return add(new _Margin(topPx+"px "+rightPx+"px "+bottomPx+"px "+leftPx+"px"));
	}

	public T MarginAuto() {
		return add(new Margin("auto"));
	}

	public T MarginBottom(String size) {
		return add(new MarginBottom(size));
	}

	public T MarginBottom(int px) {
		return add(new MarginBottom(px));
	}
	public T MarginLeft(String size) {
		return add(new MarginLeft(size));
	}
	public T MarginLeft(int px) {
		return add(new MarginLeft(px));
	}

	public T MarginRight(String size) {
		return add(new MarginRight(size));
	}

	public T MarginRight(int px) {
		return add(new MarginRight(px));
	}

	public T MarginTop(String size) {
		return add(new MarginTop(size));
	}
	public T MarginTop(int px) {
		return add(new MarginTop(px));
	}

	public T MaxHeight(String size) {
		return add(new MaxHeight(size));
	}

	public T MaxHeight(int px) {
		return add(new MaxHeight(px));
	}

	public T MaxWidth(String size) {
		return add(new MaxWidth(size));
	}
	public T MaxWidth(int px) {
		return add(new MaxWidth(px));
	}

	public T MinHeight(String size) {
		return add(new MinHeight(size));
	}

	public T MinHeight(int px) {
		return add(new MinHeight(px));
	}

	public T MinWidth(String size) {
		return add(new MinWidth(size));
	}
	public T MinWidth(int px) {
		return add(new MinWidth(px));
	}


	/**
	 * 默认值。内容不会被修剪，会呈现在元素框之外。
	 * 
	 * @return
	 */
	public T OverflowVisible() {
		return add(new Overflow("visible"));
	}

	/**
	 * 内容会被修剪，并且其余内容是不可见的。
	 * 
	 * @return
	 */
	public T OverflowHidden() {
		return add(new Overflow("hidden"));
	}

	/**
	 * 内容会被修剪，但是浏览器会显示滚动条以便查看其余的内容。
	 * 
	 * @return
	 */
	public T OverflowScroll() {
		return add(new Overflow("scroll"));
	}
	public T Padding(int px) {
		return add(new Padding(px+"px"));
	}

	public T Padding(String size) {
		return add(new Padding(size));
	}
	public T Padding(int topPx,int rightPx,int bottomPx,int leftPx) {
		return add(new Padding(topPx+"px "+rightPx+"px "+bottomPx+"px "+leftPx+"px"));
	}

	public T PaddingBottom(String size) {
		return add(new PaddingBottom(size));
	}
	public T PaddingBottom(int px) {
		return add(new PaddingBottom(px));
	}

	public T PaddingLeft(String size) {
		return add(new PaddingLeft(size));
	}

	public T PaddingLeft(int px) {
		return add(new PaddingLeft(px));
	}

	public T PaddingRight(String size) {
		return add(new PaddingRight(size));
	}
	public T PaddingRight(int px) {
		return add(new PaddingRight(px));
	}

	public T PaddingTop(String size) {
		return add(new PaddingTop(size));
	}

	public T PaddingTop(int px) {
		return add(new PaddingTop(px));
	}

	/**
	 * 生成绝对定位的元素，相对于 static 定位以外的第一个父元素进行定位。 元素的位置通过 "left", "top", "right" 以及
	 * "bottom" 属性进行规定。
	 * 
	 * @return
	 */
	public T PositionAbsolute() {
		return add(new Position("absolute"));
	}

	/**
	 * 生成绝对定位的元素，相对于浏览器窗口进行定位。 元素的位置通过 "left", "top", "right" 以及 "bottom" 属性进行规定。
	 * 
	 * @return
	 */
	public T PositionFixed() {
		return add(new Position("fixed"));
	}

	/**
	 * 生成相对定位的元素，相对于其正常位置进行定位。
	 * 
	 * @return
	 */
	public T PositionRelative() {
		return add(new Position("relative"));
	}

	/**
	 * static 默认值。没有定位，元素出现在正常的流中（忽略 top, bottom, left, right 或者 z-index 声明）。
	 * 
	 * @return
	 */
	public T PositionStatic() {
		return add(new Position("static"));
	}
	public T Right(String size) {
		return add(new Right(size));
	}
	public T Right(int px) {
		return add(new Right(px));
	}

	public T TableLayoutAutomatic() {
		return add(new TableLayout("automatic"));
	}
	public T TableLayoutFixed() {
		return add(new TableLayout("fixed"));
	}

	public T TextAlignLeft() {
		return add(new TextAlign("left"));
	}
	public T TextAlignRight() {
		return add(new TextAlign("right"));
	}
	public T TextAlignCenter() {
		return add(new TextAlign("center"));
	}
	public T TextAlignJustify() {
		return add(new TextAlign("justify"));
	}

	public T TextDecorationNone() {
		return add(new TextDecoration("none"));
	}
	public T TextDecorationUnderline() {
		return add(new TextDecoration("underline"));
	}
	public T TextDecorationOverline() {
		return add(new TextDecoration("overline"));
	}
	public T TextDecorationLineThrough() {
		return add(new TextDecoration("line-through"));
	}
	public T TextDecorationBlink() {
		return add(new TextDecoration("blink"));
	}
	public T TextIndent(String size) {
		return add(new TextIndent(size));
	}
	public T TextIndent(int px) {
		return add(new TextIndent(px));
	}

	public T Top(String size) {
		return add(new Top(size));
	}
	public T Top(int px) {
		return add(new Top(px));
	}

	/**
	 * 把元素的顶端与行中最高元素的顶端对齐
	 * @return
	 */
	public T VerticalAlignTop() {
		return add(new VerticalAlign("top"));
	}
	/**
	 * 把此元素放置在父元素的中部。
	 * @return
	 */
	public T VerticalAlignMiddle() {
		return add(new VerticalAlign("middle"));
	}
	/**
	 * 把元素的顶端与行中最低的元素的顶端对齐。
	 * @return
	 */
	public T VerticalAlignCenter() {
		return add(new VerticalAlign("center"));
	}
	/**
	 * 默认。元素放置在父元素的基线上。
	 * @return
	 */
	public T VerticalAlignBaseline() {
		return add(new VerticalAlign("baseline"));
	}
	/**
	 * 垂直对齐文本的下标。
	 * @return
	 */
	public T VerticalAlignSub() {
		return add(new VerticalAlign("sub"));
	}
	/**
	 * 垂直对齐文本的上标
	 * @return
	 */
	public T VerticalAlignSuper() {
		return add(new VerticalAlign("super"));
	}
	/**
	 * 把元素的顶端与父元素字体的顶端对齐
	 * @return
	 */
	public T VerticalAlignTextTop() {
		return add(new VerticalAlign("text-top"));
	}
	/**
	 * 把元素的底端与父元素字体的底端对齐。
	 * @return
	 */
	public T VerticalAlignTextBottom() {
		return add(new VerticalAlign("text-bottom"));
	}

	/**
	 * 默认值。元素是可见的。
	 * @return
	 */
	public T VisibilityVisible() {
		return add(new Visibility("visible"));
	}
	/**
	 * 元素是不可见的。
	 * @return
	 */
	public T VisibilityHidden() {
		return add(new Visibility("hidden"));
	}
	public T Width(String size) {
		return add(new Width(size));
	}
	public T Width(int px) {
		return add(new Width(px+"px"));
	}
	public T _Width(int px) {
		return add(new Width(px+"px"));
	}

	public T ZIndex(String s) {
		return add(new ZIndex(s));
	}
	public T ZIndex(int value) {
		return add(new ZIndex(value));
	}
}
