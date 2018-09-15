package irille.pub.html;

import irille.pub.html.Exts.ExtBase;

/**
 * JS函数
 * 
 * @author whx
 * 
 */
public class ExtFunCall<T extends ExtFunCall>  extends ExtBase {
	private ExtParas _paras;
	private ExtList _parent = null;
	public ExtFunCall(ExtList parent, String cmd, Object... paras) {
		_parent = parent;
		_paras=new ExtParas(cmd,paras);
	}

	/**
	 * 增加函数参数
	 * @param paras
	 * @return
	 */
	public T addFunParas(Object... paras) {
		_paras.add(paras);
		return (T)this;
	}
	/**
	 * 增加表达式类型的参数
	 * @param paras
	 * @return
	 */
	public T addFunParasExp(String... paras) {
		for(String para:paras)
			_paras.add(new ExtExp(para));
		return (T)this;
	}
	
	/**
	 * 增加List类型的参数
	 * @return
	 */
	public ExtList AddFunParaList() {
		ExtList list=new ExtList(null);
		_paras.add(list);
		return list;
	}
	/* (non-Javadoc)
	 * @see irille.pub.html.ExtList#out(int, java.lang.StringBuilder)
	 */	
	@Override
	public void out(int tabs, StringBuilder buf) {
		tabs++;
		_paras.out(tabs, buf);
	}	
}
