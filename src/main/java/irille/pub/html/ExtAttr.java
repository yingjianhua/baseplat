package irille.pub.html;

import irille.pub.ext.Ext;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.Exts.ExtBase;

/**
 * 属性类, 输出的代码格式为："名称 : 值,"
 * 
 * @author whx
 * 
 */
public class ExtAttr extends ExtBase{
	private Object _v; //值
	private String _name; //名称

	/**
	 * 构造值为空的属性
	 * @param name 属性名
	 */
	public ExtAttr(String name) {
		_name=name;
	}
	/**
	 * 构造方法 
	 * @param name 属性名
	 * @param value 值
	 */
	public ExtAttr(String name,Object value) {
		_name=name;
		_v = value;
	}

	/**
	 * 置表达式，在输出时两端不带“‘”
	 * @param value
	 * @return
	 */
	public ExtAttr setExp(String value) {
		_v=new ExtExp(value);
		return this;
	}

	
	/**
	 * 将置设为数组，数组由“[”“]”括起来，以“,”分隔
	 * @param exps
	 * @return
	 */
	public ExtDime SetDime(Object... exps) {
		_v=new ExtDime(exps);
		return (ExtDime)_v;
	}
	/**
	 * 置列表，内容可以为属性定义及函数等
	 * @param parent
	 * @return
	 */
	public ExtList SetList(ExtList parent) {
		_v=new ExtList(parent);
		return (ExtList)_v;
	}

	/**
	 * 置函数
	 * @param parent 父列表
	 * @param name 函数名
	 * @param paras 参数
	 * @return
	 */
	public ExtFunCall setFunCall(ExtList parent,String name, Object... paras) {
		_v=new ExtFunCall(parent,name,paras);
		return (ExtFunCall)_v;
	}
	
	/**
	 * 置函数
	 * @param parent 父列表
	 * @return
	 */
	public ExtFunCall setFunCall(ExtList parent) {
		return setFunCall(parent);
	}
	
	/**
	 * 取属性值
	 * 
	 * @return 属性值
	 */
	public Object get() {
		return _v;
	}

	/**
	 * 取属性名
	 * 
	 * @return 属性名
	 */
	public String getName() {
		return _name;
	}

	/**
	 * 输出为字符串，结果用于Html文件的Style标签中
	 */
	@Override
	public String toString() {
		return getName() + " : " + _v.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtI#out(int)
	 */
	@Override
	public void out(int tabs,StringBuilder buf) {
		if(_v instanceof IExtOut) {
			buf.append(getName()+" : ");
			((IExtOut)_v).out(tabs,buf);
			return;
		}
		buf.append(getName()+" : ");
		Ext.outObject(_v, tabs, buf);
	}
}
