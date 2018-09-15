package irille.pub.html;

import irille.pub.ext.Ext.IExtOut;

import java.util.Vector;

/**
 * JS函数
 * 
 * @author whx
 * 
 */
public class ExtFunDefine extends ExtFunCall<ExtFunDefine> {
	private Vector _list = new Vector();
	private String _name;
	public ExtFunDefine(ExtList parent, String name, Object... paras) {
		super(parent,"function",paras);
		_name=name;
	}

	/**
	 * 增加函数参数
	 * @param cmd
	 * @param paras
	 * @return
	 */
	public ExtParas addParas(String cmd, Object... paras) {
		ExtParas p=new ExtParas(cmd,paras);
		add(p);
		return p;
	}
	
	/**
	 * 增加数组对象
	 * @param paras
	 * @return
	 */
	public ExtDime AddDime(Object... paras) {
		ExtDime p=new ExtDime(paras);
		add(p);
		return p;
	}
	
	/**
	 * 增加函数代码行内容
	 * @param exps
	 * @return内容
	 */
	public ExtFunDefine add(Object... exps) {
		for(Object obj:exps)
		_list.add(obj);
		return this;
	}
/**
 * 增加函数代码行内容
 * @param line
 * @return
 */
	public ExtFunDefine add(IExtOut line) {
		_list.add(line);
		return this;
	}
	/* (non-Javadoc)
	 * @see irille.pub.html.ExtList#out(int, java.lang.StringBuilder)
	 */
	@Override
	public void out(int tabs, StringBuilder buf) {
		buf.append(_name+" : ");
		super.out(tabs, buf);
		buf.append("{"+LN);
		for (Object ext : _list) {
			if(ext instanceof IExtOut) {
				if(!(ext.getClass().equals(ExtDime.class))){ //对齐更好看
					buf.append(T[tabs]);
				}
				((IExtOut)ext).out(tabs, buf);
				buf.append(";"+LN);
			} else
				buf.append(ext.toString());
		}
		buf.append(T[tabs-1]+"}");
	}

	
	/**
	 * 取定义的函数名
	 * @return the name
	 */
	public String getName() {
		return _name;
	}
}
