package irille.pub.html;


/**
 * 表达式或变量（不包括JAVA基本类型）,输出时不会用单引号括起来
 * 
 * @author whx
 * 
 */
public class ExtExp {
	private String _exp;

	public ExtExp(String exp) {
		_exp=exp;
	}
	/**
	 * 取属性值
	 * 
	 * @return 属性值
	 */
	public Object get() {
		return _exp;
	}

	/**
	 * 输出为字符串，结果用于Html文件的Style标签中
	 */
	@Override
	public String toString() {
		return _exp;
	}
}
