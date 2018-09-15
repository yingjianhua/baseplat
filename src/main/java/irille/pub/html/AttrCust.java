package irille.pub.html;

/**
 * 自定义属性
 * 
 * @author whx
 * 
 */
public class AttrCust extends AttrBase {
	private String _name;

	/**
	 * 构造方法
	 * @param name 属性名
	 * @param value 属性值
	 */
	public AttrCust(String name, String value) {
		super(value);
		_name = name;
	}

	@Override
	public String getName() {
		return _name;
	}
}
