package irille.pub;

import irille.pub.PubInfs.IMsg;
import irille.pub.inf.ICn;

import java.text.MessageFormat;

/**
 * 代码与名称对象
 * @author whx
 *
 */
public class Cn implements ICn,IMsg{
	private String _code;
	private String _name;
	/**
	 * 构造方法
	 * @param code
	 * @param name
	 */
	public Cn(String code, String name) {
		_code=code;
		_name=name;
	}
	/**
	 * 构造方法
	 * @param code
	 * @param name
	 */
	public Cn(Object code, Object name) {
		_code=code.toString();
		_name=name.toString();
	}
	/**
	 * 构造方法
	 * @param code 代码
	 * @param name 名称，可以带以{}括起来的参数，从{0}开始
	 * @param paras 参数
	 */
	public Cn(String code,String name, Object... paras) {
		this(code,new MessageFormat(name).format(paras).toString());
	}
	public String getCode() {
  	return _code;
  }
	public String getName() {
  	return _name;
  }
	/**
	 * 返回代码，兼容 IMsg接口
	 */
	public String name() {
		return _code;
	}
	public String getMsg() {
		return _name;
	}
	public String getCodeName() {
		return _code+":"+_name;
	}
}
