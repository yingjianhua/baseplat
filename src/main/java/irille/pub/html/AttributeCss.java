package irille.pub.html;

import irille.pub.Log;


/**
 * Css 中的各种属性的类，都继承于AttrBase
 * 
 * @author whx
 * 
 */
public class AttributeCss {
	/**
	 * 属性为嵌入的Node,  Jsp的<s:if... 有用到此语法
	 * @author whx
	 *
	 */
	public static class AttrNode extends AttrBase {
		private static final Log LOG = new Log(AttrNode.class);

		private XmlNode _node;
		public AttrNode(XmlNode node) {
	    super(node.getXmlLabel());
	    _node=node;
    }

		@Override
    public String getName() {
	    return _node.getXmlLabel();
    }
		@Override
		public String outCssCode() {
		  throw LOG.err();  //不支持在Css文件中应用
		}

		@Override
		public String toString() {
		  return _node.toString();
		}		
	}

	
	// 背景颜色
	public static class Backgroud extends AttrBase {
		public Backgroud(String str) {
			super(str);
		}

		@Override
		public String getName() {
			return "background";
		}
	}

	// 背景颜色
	public static class BackgroudColor extends AttrBase {
		public BackgroudColor(String color) {
			super(color);
		}

		@Override
		public String getName() {
			return "background-color";
		}
	}

	// 页面上对背景图像进行平铺
	public static class BackgroudRepeat extends AttrBase {

		public BackgroudRepeat(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "background-repeat";
		}
	}

	// background-position 背景定位
	public static class BackgroudPosition extends AttrBase {
		public static final BackgroudPosition LEFT = new BackgroudPosition("left");
		public static final BackgroudPosition LEFT_TOP_DEFAULT = new BackgroudPosition(
		    "left top");
		public static final BackgroudPosition LEFT_BOTTOM = new BackgroudPosition(
		    "left bottom");
		public static final BackgroudPosition RIGHT = new BackgroudPosition("right");
		public static final BackgroudPosition RIGHT_TOP = new BackgroudPosition(
		    "right top");
		public static final BackgroudPosition RIGHT_BOTTOM = new BackgroudPosition(
		    "right bottom");
		public static final BackgroudPosition TOP = new BackgroudPosition("top");
		public static final BackgroudPosition BOTTOM = new BackgroudPosition(
		    "bottom");
		public static final BackgroudPosition CENTER = new BackgroudPosition(
		    "center");

		public BackgroudPosition(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "background-position";
		}
	}

	// 背景关联：如果文档比较长，那么当文档向下滚动时，背景图像也会随之滚动。当文档滚动到超过图像的位置时，图像就会消失。
	public static class BackgroudAttachment extends AttrBase{

		public BackgroudAttachment(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "background-attachment";
		}
	}

	public static class Cursor extends AttrBase {
		public Cursor(String str) {
			super(str);
		}

		@Override
		public String getName() {
			return "cursor";
		}
	}

	public static class TextIndent extends AttrBase {
		public TextIndent(int px) {
			super(px + "px");
		}

		public TextIndent(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "text-indent";
		}
	}

	// text-align 文本对齐
	public static class TextAlign extends AttrBase {
		public static final TextAlign LEFT_DEFAULT = new TextAlign("left");
		public static final TextAlign RIGHT = new TextAlign("right");
		public static final TextAlign CENTER = new TextAlign("center");
		public static final TextAlign JUSTIFY = new TextAlign("justify");

		public TextAlign(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "text-align";
		}
	}

	// letter-spacing 字母间格
	public static class LetterSpacing extends TextIndent {
		public LetterSpacing(int px) {
			super(px);
		}

		public LetterSpacing(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "letter-spacing";
		}
	}

	// text-decoration 文本装饰
	public static class TextDecoration extends AttrBase {
		public static final TextDecoration NONE = new TextDecoration("none");
		public static final TextDecoration UNDERLINE = new TextDecoration(
		    "underline");
		public static final TextDecoration OVERLINE = new TextDecoration("overline");
		public static final TextDecoration LINE_THROUGH = new TextDecoration(
		    "line-through");
		public static final TextDecoration BLINK = new TextDecoration("blink");

		public TextDecoration(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "text-decoration";
		}

	}

	// line-height 行高
	public static class LineHeight extends TextIndent {
		public static final LineHeight NORMAL = new LineHeight("normal");

		public LineHeight(int px) {
			super(px);
		}

		public LineHeight(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "line-height";
		}
	}

	// margin-right 外边框右边大小
	public static class Filter extends AttrBase {

		public Filter(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "filter";
		}
	}

	
	// font-family 字体
	public static class FontFamily extends AttrBase {

		public FontFamily(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "font-family";
		}
	}

	// font-style 字体风格。
	public static class FontStyle extends AttrBase {
		public static final FontStyle NORMAL = new FontStyle("normal"); // 文本正常显示
		public static final FontStyle ITALIC = new FontStyle("italic");// 文本斜体显示

		// public static final Style OBLIQUE = new Style("oblique");//文本倾斜显示 XXX
		// 结果与上行差不多
		public FontStyle(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "font-style";
		}
	}

	// font-weight 字体风格。
	public static class FontWeight extends AttrBase{
		public static final FontWeight NORMAL = new FontWeight("normal"); // 文本正常显示
		public static final FontStyle BOLD = new FontStyle("bold");// 文本加粗显示

		public FontWeight(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "font-weight";
		}
	}

	// font-size 字体大小
	public static class FontSize extends TextIndent {
		public static final FontSize PX14 = new FontSize(14);
		public static final FontSize PX16 = new FontSize(16);
		public static final FontSize DEFAULT_16 = new FontSize(16);
		public static final FontSize PX18 = new FontSize(18);
		public static final FontSize PX20 = new FontSize(20);

		public FontSize(int px) {
			super(px);
		}

		public FontSize(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "font-size";
		}
	}

	// list-style-type 列表类型
	public static class ListStyleType extends AttrBase{

		public ListStyleType(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "list-style-type";
		}
	}

	// 设置在何处放置列表项标记
	public static class ListStylePosition extends AttrBase {
		// 列表项目标记放置在文本以内，且环绕文本根据标记对齐。
		public static final ListStylePosition INSIDE = new ListStylePosition(
		    "inside");
		// 默认值。保持标记位于文本的左侧。列表项目标记放置在文本以外，且环绕文本不根据标记对齐。
		public static final ListStylePosition OUTSIDE_DEFAULT = new ListStylePosition(
		    "outside");

		public ListStylePosition(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "list-style-position";
		}
	}

	// width 宽度
	public static class Width extends TextIndent{
		public static final Width FILL100 = new Width("100%");

		public Width(int px) {
			super(px);
		}

		public Width(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "width";
		}
	}
	// width 宽度
	public static class _Width extends TextIndent{
	
		public _Width(int px) {
			super(px);
		}

		public _Width(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "_width";
		}
	}


	// height 高度
	public static class Height extends TextIndent {
		public static final Height FILL100 = new Height("100%");

		public Height(int px) {
			super(px);
		}

		public Height(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "height";
		}
	}

	// min-width 设置元素的最小宽度。
	public static class MinWidth extends TextIndent{

		public MinWidth(int px) {
			super(px);
		}

		public MinWidth(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "min-width";
		}
	}

	// min-height 设置元素的最小高度。 2
	public static class MinHeight extends TextIndent {

		public MinHeight(int px) {
			super(px);
		}

		public MinHeight(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "min-height";
		}
	}

	// max-width 设置元素的最大宽度。 2
	public static class MaxWidth extends TextIndent{
		public MaxWidth(int px) {
			super(px);
		}

		public MaxWidth(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "max-width";
		}
	}

	public static class MaxHeight extends TextIndent {
		public MaxHeight(int px) {
			super(px);
		}

		public MaxHeight(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "max-height";
		}
	}

	// 所有浏览器都支持 table-layout 属性。
	public static class TableLayout extends AttrBase {
		public static final TableLayout AUTOMATIC_DEFAULT = new TableLayout(
		    "automatic");
		public static final TableLayout FIXED = new TableLayout("fixed");

		public TableLayout(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "table-layout";
		}
	}

	// border-collapse 属性设置表格的边框是否被合并为一个单一的边框，还是象在标准的 HTML 中那样分开显示。
	public static class BorderCollapse extends AttrBase {

		public BorderCollapse(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "border-collapse";
		}
	}

	public static class Overflow extends AttrBase {

		public Overflow(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "overflow";
		}
	}

	// 属性剪裁绝对定位元素。
	public static class Clip extends AttrBase{
		public static final Clip DIME16_16 = new Clip(0, 16, 16, 0);
		public static final Clip DIME32_32 = new Clip(0, 32, 32, 0);
		public static final Clip DIME40_40 = new Clip(0, 40, 40, 0);
		public static final Clip DIME50_50 = new Clip(0, 50, 50, 0);
		public Clip(String val) {
			super(val);
		}
		public Clip(int top, int right, int bottom, int left) {
			super("rect(" + top + "px," + right + "px," + bottom + "px," + left
			    + "px");
		}

		@Override
		public String getName() {
			return "clip";
		}
	}

	public static class Clear extends AttrBase {
		public static final Clear NONE_DEFAULT = new Clear("none");
		public static final Clear LEFT = new Clear("left");
		public static final Clear RIGHT = new Clear("right");
		public static final Clear BOTH = new Clear("both");

		public Clear(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "clear";
		}
	}

	public static class Display extends AttrBase {
		public Display(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "display";
		}
	}

	public static class _Display extends AttrBase {
		public _Display(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "_display";
		}
	}
	
	// position 浮动
	public static class Float_ extends AttrBase {
		public Float_(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "float";
		}
	}

	// position 定位
	public static class Position extends AttrBase {

		public Position(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "position";
		}
	}

	// 属性规定元素是否可见。
	public static class Visibility extends AttrBase{
		// 默认值。元素是可见的。
		public static final Visibility VISIBLE_DEFAULT = new Visibility("visible");
		// 元素是不可见的。
		public static final Visibility HIDDEN = new Visibility("hidden");

		public Visibility(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "visibility";
		}
	}

	// z-index 属性设置元素的堆叠顺序。拥有更高堆叠顺序的元素总是会处于堆叠顺序较低的元素的前面。
	// 注释：元素可拥有负的 z-index 属性值。
	// 注释：Z-index 仅能在定位元素上奏效（例如 position:absolute;）！
	public static class ZIndex extends AttrBase{
		// 默认。堆叠顺序与父元素相等。
		public static final ZIndex AUTO_DEFAULT = new ZIndex("auto");
		public static final ZIndex Z_1 = new ZIndex(-1);
		public static final ZIndex Z10 = new ZIndex(10);
		public static final ZIndex Z20 = new ZIndex(20);

		public ZIndex(String v) {
			super(v);
		}

		public ZIndex(int v) {
			super("" + v);
		}

		@Override
		public String getName() {
			return "z-index";
		}
	}

	// 设置定位元素下外边距边界与其包含块下边界之间的偏移。
	public static class Bottom extends TextIndent{
		public static final Bottom AUOT_0_DEFAULT = new Bottom("auto");

		public Bottom(int px) {
			super(px);
		}

		public Bottom(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "bottom";
		}
	}

	// 设置定位元素的上外边距边界与其包含块上边界之间的偏移。
	public static class Top extends TextIndent {
		public static final Top AUOT_0_DEFAULT = new Top("auto");

		public Top(int px) {
			super(px);
		}

		public Top(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "top";
		}
	}

	// 设置定位元素左外边距边界与其包含块左边界之间的偏移。
	public static class Left extends TextIndent {
		public static final Left AUOT_0_DEFAULT = new Left("auto");

		public Left(int px) {
			super(px);
		}

		public Left(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "left";
		}
	}

	// 设置定位元素右外边距边界与其包含块右边界之间的偏移。
	public static class Right extends TextIndent {
		public static final Right AUOT_0_DEFAULT = new Right("auto");

		public Right(int px) {
			super(px);
		}

		public Right(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "right";
		}
	}

	// vertical-align:文本垂直对齐
	public static class VerticalAlign extends TextIndent{
		// 把元素的顶端与行中最高元素的顶端对齐
		public static final VerticalAlign TOP = new VerticalAlign("top");
		// 把此元素放置在父元素的中部。
		public static final VerticalAlign MIDDLE = new VerticalAlign("middle");
		// 把元素的顶端与行中最低的元素的顶端对齐。
		public static final VerticalAlign CENTER = new VerticalAlign("center");
		// 默认。元素放置在父元素的基线上。
		public static final VerticalAlign BASELINE = new VerticalAlign("baseline");
		// 垂直对齐文本的下标。
		public static final VerticalAlign SUB = new VerticalAlign("sub");
		// 垂直对齐文本的上标
		public static final VerticalAlign SUPER = new VerticalAlign("super");
		// 把元素的顶端与父元素字体的顶端对齐
		public static final VerticalAlign TEXT_TOP = new VerticalAlign("text-top");
		// 把元素的底端与父元素字体的底端对齐。
		public static final VerticalAlign TEXT_BOTTOM = new VerticalAlign(
		    "text-bottom");

		// length
		// % 使用 "line-height" 属性的百分比值来排列此元素。允许使用负值。

		public VerticalAlign(int px) {
			super(px);
		}

		public VerticalAlign(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "vertical-align";
		}
	}

	// paddingTop 上边距
	public static class PaddingTop extends TextIndent {

		public PaddingTop(int px) {
			super(px);
		}

		public PaddingTop(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "padding-top";
		}
	}

	// padding-bottom 下边距
	public static class PaddingBottom extends TextIndent{

		public PaddingBottom(int px) {
			super(px);
		}

		public PaddingBottom(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "padding-bottom";
		}
	}

	// padding-left 内边距
	public static class PaddingLeft extends TextIndent{
		public PaddingLeft(int px) {
			super(px);
		}

		public PaddingLeft(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "padding-left";
		}
	}

	// padding-right 右边距
	public static class PaddingRight extends TextIndent{

		public PaddingRight(int px) {
			super(px);
		}

		public PaddingRight(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "padding-right";
		}
	}

	// padding 内边距
	public static class Padding extends TextIndent {

		public Padding(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "padding";
		}
	}

	// margin 外边框大小
	public static class Margin extends TextIndent {
		public static final Margin PX3 = new Margin(14);

		public Margin(int px) {
			super(px);
		}

		public Margin(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "margin";
		}
	}
	// margin 外边框大小
	public static class _Margin extends TextIndent {

		public _Margin(int px) {
			super(px);
		}

		public _Margin(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "_margin";
		}
	}

	// margin-bottom 外边框下方大小
	public static class MarginBottom extends TextIndent {

		public MarginBottom(int px) {
			super(px);
		}

		public MarginBottom(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "margin-bottom";
		}
	}

	// margin-left 外边框左边大小
	public static class MarginLeft extends TextIndent{
		public MarginLeft(int px) {
			super(px);
		}

		public MarginLeft(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "margin-left";
		}
	}

	// margin-right 外边框右边大小
	public static class MarginRight extends TextIndent {

		public MarginRight(int px) {
			super(px);
		}

		public MarginRight(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "margin-right";
		}
	}

	// margin-top 外边框上方大小
	public static class MarginTop extends TextIndent{

		public MarginTop(int px) {
			super(px);
		}

		public MarginTop(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "margin-top";
		}
	}

	public static class Border extends AttrBase {
		public Border(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "border";
		}
	}
	public static class BorderTop extends AttrBase{
		public BorderTop(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "border-top";
		}
	}
	public static class BorderRight extends AttrBase{
		public BorderRight(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "border-right";
		}
	}
	public static class BorderLeft extends AttrBase{
		public BorderLeft(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "border-left";
		}
	}
	public static class BorderBottom extends AttrBase {
		public BorderBottom(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "border-bottom";
		}
	}

	// hidden 与 "none" 相同。不过应用于表时除外，对于表，hidden 用于解决边框冲突。
	// dotted 定义点状边框。在大多数浏览器中呈现为实线。
	// dashed 定义虚线。在大多数浏览器中呈现为实线。
	// XXX 考虑到不兼容问题，有些没有定义
	public static class BorderStyle extends AttrBase {
		public static final BorderStyle NONE_DEFAULT = new BorderStyle("none"); // 没有线
		public static final BorderStyle SOLID = new BorderStyle("solid");// 实线
		public static final BorderStyle DOUBLE = new BorderStyle("double");// 双实线

		public BorderStyle(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "border-style";
		}
	}

	// width 宽度
	public static class BorderWidth extends TextIndent{
		public static final BorderWidth PX1 = new BorderWidth(1);

		public BorderWidth(int px) {
			super(px);
		}

		public BorderWidth(String v) {
			super(v);
		}

		@Override
		public String getName() {
			return "border-width";
		}
	}

	// border-color 边框颜色
	public static class BorderColor extends AttrBase {

		public BorderColor(String color) {
			super(color);
		}

		@Override
		public String getName() {
			return "border-color";
		}
	}
}
