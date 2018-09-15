package irille.pub.html;

/**
 * 属性类的基类,CSS中的每种标签都可以设置属性，如：“margin: 2em 0em 1em 0em;”，“font-size: 1em;”等
 * 
 * @author whx
 * 
 */
public abstract class AttrBase {
	private String _v;
	private boolean _ie6 = false; 

	/**
	 * 构造方法
	 * @param value 属性的值
	 */
	public AttrBase(String value) {
		_v = value;
	}

	
	/**
	 * 输出Css文件中的属性值行,如“float:left;"，如果调用过方法"_()"，则属性名前加"_"用于兼容 IE6
	 * @return
	 */
	public String outCssCode() {
		return (_ie6 ? "_" : "") + getName() + ":" + _v;
	}

	/**
	 * 考虑IE6的兼容，当设定此标志时，输入的标签前加"_"
	 * @return 当前对象
	 */
	public final void _() {
		_ie6 = true;
	}

	/**
	 * 取属性值
	 * @return 属性值
	 */
	public String getValue() {
		return _v;
	}
/**
 * 取属性名
 * @return 属性名
 */
	public abstract String getName();

	/**
	 * 输出为字符串，结果用于Html文件的Style标签中
	 */
	@Override
	public String toString() {
		return (_ie6 ? "_" : "") + getName() + "=" + '"' + _v + '"';
	}
}
