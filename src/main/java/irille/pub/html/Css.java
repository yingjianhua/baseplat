package irille.pub.html;

import irille.pub.FileTools;
import irille.pub.IPubVars;
import irille.pub.svr.Env;

import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestResult;

/**
 * 产生Css文件的基类，Css文件中包括若干CssLine的行
 * 要重构init方法对元素行进行定义
 * @author whx
 * @param <THIS>
 */
public class Css<THIS extends Css> extends AttributeCss implements Test, IPubVars{	
	public static final String TAB = "  ";
	private Vector<CssLine> _as = null;
	private String _fileName = null;

	/**
	 * 构造方法，会调用init()方法进行初始化
	 */
	public Css() {
		init();
	}
	
	/**
	 * 属性行初始化，子类要重构此方法，来调定各行
	 */
	public void init() {		
	}
	
	public int countTestCases() {
	  if(_fileName==null)
	  	System.out.println(outCssCode(""));
	  else
	  	save("	");
	  return 0;
  }

	public final void testCase() {
	}

	
	public void run(TestResult arg0) {
  }

	/**
	 * 设定输出的文件名
	 * @param fileName 文件名
	 * @return THIS 本对象
	 */
	public THIS setFileName(String fileName) {
		_fileName = fileName;
		return (THIS) this;
	}

/**
 * 增加CssLine
 * @param elelementName 元素名
 * @param as 属性列表
 * @return CssLine
 */
	public CssLine add(String elelementName, AttrBase... as) {
		CssLine css = new CssLine(this,elelementName, as);
		if (_as == null)
			_as = new Vector<CssLine>();
		_as.add(css);
		return css;
	}
	/**
	 * 增加CssLine 
	 * @param elelementName 元素名
	 * @param fromCssLine 来源的元素行
	 * @return CssLine
	 */
	public CssLine add(String elelementName,CssLine fromCssLine) {
		CssLine css = new CssLine(this,elelementName,fromCssLine);
		if (_as == null)
			_as = new Vector<CssLine>();
		_as.add(css);
		return css;
	}
	/**
	 * 建立属性集，仅用于快速复制属性，不加到Css文件中
	 * @return 返回新的属性集
	 */
	public static CssLine tmpAttributes() {
		CssLine css = new CssLine(null," ");		
		return css;
	}

	/**
	 * 取文件名
	 * @return 文件名
	 */
	public String getFileName() {
		return _fileName;
	}
	/**
	 * 取Id
	 * @param name 元素名
	 * @return 结果
	 */
	public CssLine getId(String name) {
		return get(CssLine.ID,name);
	}
	/**
	 * 取类别
	 * @param name 元素名
	 * @return 结果
	 */
	public CssLine getClazz(String name) {
		return get(CssLine.CLASS,name);
	}
	/**
	 * 取元素
	 * @param name 元素名
	 * @return 结果
	 */
	public CssLine getElelement(String name) {
		return get(CssLine.ELELEMENT,name);
	}
	/**
	 * 取注释
	 * @param name 注释名
	 * @return 结果
	 */
	public CssLine getRem(String name) {
		return get(CssLine.REM,name);
	}
	
	
	private CssLine get(String type,String name) {
		for(CssLine line:_as) {
			if(line.getName().equals(name) && line.getType().equals(type))
				return line;
			for(CssLine link:line.getLinks()) //
				if(link.getName().equals(name) && link.getType().equals(type))
					return link;				
		}
		return null;
	}

	public CssLine a(AttrBase... as) {
		return add("a", as);
	}

	public CssLine p(AttrBase... as) {
		return add("p", as);
	}

	public CssLine h1(AttrBase... as) {
		return add("h1", as);
	}

	public CssLine h2(AttrBase... as) {
		return add("h2", as);
	}

	public CssLine h3(AttrBase... as) {
		return add("h3", as);
	}

	public CssLine h4(AttrBase... as) {
		return add("h4", as);
	}

	public CssLine h5(AttrBase... as) {
		return add("h5", as);
	}

	public CssLine h6(AttrBase... as) {
		return add("h6", as);
	}

	public CssLine body(AttrBase... as) {
		return add("body", as);
	}

	public CssLine img(AttrBase... as) {
		return add("img", as);
	}

	public CssLine ul(AttrBase... as) {
		return add("ul", as);
	}

	public CssLine li(AttrBase... as) {
		return add("li", as);
	}

	public CssLine table(AttrBase... as) {
		return add("table", as);
	}

	public CssLine th(AttrBase... as) {
		return add("th", as);
	}

	public CssLine td(AttrBase... as) {
		return add("td", as);
	}

	public CssLine div(AttrBase... as) {
		return add("div", as);
	}

	public CssLine ol(AttrBase... as) {
		return add("ol", as);
	}

	public CssLine dl(AttrBase... as) {
		return add("dl", as);
	}

	public CssLine dt(AttrBase... as) {
		return add("dt", as);
	}

	public CssLine dd(AttrBase... as) {
		return add("dd", as);
	}
	public CssLine span(AttrBase... as) {
		return add("span", as);
	}
/**
 * 增加注释
 * @param text 注释内容
 */
	public void rem(String text) {
		add(CssLine.REM+text);
	}
	/**
	 * 设定类型
	 * @param className 类型名
	 * @param as 属性列表
	 * @return 新建的CssLine
	 */
	public CssLine clazz(String className, AttrBase... as) {
		return add(CssLine.CLASS + className, as);
	}
	/**
	 * 设定类型
	 * @param className  类型名
	 * @param cssLine 来源的cssLine
	 * @return  新建的CssLine
	 */
	public CssLine clazz(String className, CssLine cssLine) {
		return add(CssLine.CLASS + className, cssLine);
	}
	
	/**
	 * 设定Id
	 * @param idName Id名
	 * @param as 属性列表
	 * @return 新建的CssLine
	 */
	public CssLine id(String idName, AttrBase... as) {
		return add(CssLine.ID + idName, as);
	}
	/**
	 * 设定Id
	 * @param idName Id名
	 * @param cssLine 来源的cssLine
	 * @return 新建的CssLine
	 */
	public CssLine id(String idName, CssLine cssLine) {
		return add(CssLine.ID + idName, cssLine);
	}

	/**
	 * 输出Css代码
	 * @param tab 缩进的空格字符串
	 * @return 代码
	 */
	public String outCssCode(String tab) {
		if(_as==null)
			return "";
		StringBuilder buf = new StringBuilder();
		for (CssLine a : _as)
			buf.append(a.outCssCode(tab));
		return buf.toString();
	}
	
	/**
	 * cssLine的元素行数量
	 * @return 结果
	 */
	public int size() {
		if(_as==null)
			return 0;
		return _as.size();
	}

	/**
	 * 取指定的cssLine
	 * @param i 位置，从0开始
	 * @return 结果
	 */
	public CssLine get(int i) {
		return _as.get(i);
	}
	
/**
 * 输出到文件
 * @param tab 缩进的空格数
 */
	public final void save(String tab) {
		System.out.println("产生CSS文件:" + _fileName);
		FileTools.writeStr(_fileName, outCssCode(tab));
	}
	
	/**
	 * 该类用于快速构建常用的尺寸属性，其本身不是CSS的属性！ 请将常用属性定义在此类中
	 * 
	 * @author whx
	 * 
	 */
	public static class Dime {
		public static final Dime PX0 = new Dime(0);
		public static final Dime PX1 = new Dime(1);
		public static final Dime PX2 = new Dime(2);
		public static final Dime PX3 = new Dime(3);
		public static final Dime PX4 = new Dime(4);
		public static final Dime PX5 = new Dime(5);
		public static final Dime PX6 = new Dime(6);
		public static final Dime PX7 = new Dime(7);
		public static final Dime PX8 = new Dime(8);
		public static final Dime PX9 = new Dime(9);
		public static final Dime PX10 = new Dime(10);
		public static final Dime PX11 = new Dime(11);
		public static final Dime PX12 = new Dime(12);
		public static final Dime PX13 = new Dime(13);
		public static final Dime PX14 = new Dime(14);
		public static final Dime PX15 = new Dime(15);
		public static final Dime PX16 = new Dime(16);
		public static final Dime PX20 = new Dime(20);
		public static final Dime PX25 = new Dime(25);
		public static final Dime PX30 = new Dime(30);
		public static final Dime PX32 = new Dime(32);
		public static final Dime PX35 = new Dime(35);
		public static final Dime PX40 = new Dime(40);
		public static final Dime PX45 = new Dime(45);
		public static final Dime PX48 = new Dime(48);
		public static final Dime PX50 = new Dime(50);
		public static final Dime PER50 = new Dime("50%");
		public static final Dime PER80 = new Dime("80%");
		public static final Dime PER100 = new Dime("100%");
		public static final Dime PER150 = new Dime("150%");
		public static final Dime PER200 = new Dime("200%");

		private String _v;

		public Dime(int px) {
			_v = px + "px";
		}

		public Dime(String v) {
			_v = v;
		}

		public String getValue() {
			return _v;
		}

		public TextIndent newTextIndent() {
			return new TextIndent(_v);
		}

		public BorderWidth newBorderWidth() {
			return new BorderWidth(_v);
		}

		public Bottom newBottom() {
			return new Bottom(_v);
		}

		public FontSize newFontSize() {
			return new FontSize(_v);
		}

		public Height newHeight() {
			return new Height(_v);
		}

		public Left newLeft() {
			return new Left(_v);
		}

		public LetterSpacing newLetterSpacing() {
			return new LetterSpacing(_v);
		}

		public LineHeight newLineHeight() {
			return new LineHeight(_v);
		}

		public Margin newMargin() {
			return new Margin(_v);
		}

		public MarginBottom newMarginBottom() {
			return new MarginBottom(_v);
		}

		public MarginLeft newMarginLeft() {
			return new MarginLeft(_v);
		}

		public MarginRight newMarginRight() {
			return new MarginRight(_v);
		}

		public MarginTop newMarginTop() {
			return new MarginTop(_v);
		}

		public MaxHeight newMaxHeight() {
			return new MaxHeight(_v);
		}

		public MaxWidth newMaxWidth() {
			return new MaxWidth(_v);
		}

		public MinHeight newMinHeight() {
			return new MinHeight(_v);
		}

		public MinWidth newMinWidth() {
			return new MinWidth(_v);
		}

		public Padding newPadding() {
			return new Padding(_v);
		}

		public PaddingBottom newPaddingBottom() {
			return new PaddingBottom(_v);
		}

		public PaddingLeft newPaddingLeft() {
			return new PaddingLeft(_v);
		}

		public PaddingRight newPaddingRight() {
			return new PaddingRight(_v);
		}

		public PaddingTop newPaddingTop() {
			return new PaddingTop(_v);
		}

		public Right newRight() {
			return new Right(_v);
		}

		public Top newTop() {
			return new Top(_v);
		}

		public VerticalAlign newVerticalAlign() {
			return new VerticalAlign(_v);
		}

		public Width newWidth() {
			return new Width(_v);
		}
	}

	public static class Color extends AttrBase {
		public static final Color PINK = new Color("#FFC0CB"); // 粉红
		public static final Color PURPLE = new Color("#800080"); // 紫色
		public static final Color BLUE = new Color("#0000FF"); // 蓝色
		public static final Color GREEN = new Color("#008000");// 纯绿
		public static final Color YELLOW = new Color("#FFFF00"); // 纯黄
		public static final Color GOLD = new Color("#FFD700");// 金色
		public static final Color ORAGE = new Color("#FFD700");// 橙色
		public static final Color RED = new Color("#FF0000");// 纯红
		public static final Color BROWN = new Color("#A52A2A");// 棕色
		public static final Color DARK_RED = new Color("#8B0000");// 深红色
		public static final Color WHITE = new Color("#FFFFFF");// 纯白
		public static final Color GRAY = new Color("#808080");// 灰色
		public static final Color BLACK = new Color("#000000");// 纯黑
		public static final Color CYAR = new Color("#00FFFF");// 青色

		public Color(String color) {
			super(color);
		}

		@Override
		public String getName() {
			return "color";
		}

		public BackgroudColor newBackgroudColor() {
			return new BackgroudColor(getValue());
		}

		public BorderColor newBorderColor() {
			return new BorderColor(getValue());
		}
	}


}
