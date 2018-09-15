//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.svr.OptCustCtrl;
import irille.pub.tb.Infs.IFldOpt;
import irille.pub.tb.Infs.IOptLine;
import irille.pub.view.VFld;
import irille.pub.view.VFldOptCust;

/**
 * Title: 自定义选项类型<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class FldOptCust<T extends FldOptCust> extends FldChar<T> implements IFldOpt {
	private String _optCode;

	/**
	 * 构造方法
	 */
	public FldOptCust(String code, String name, String optCode, int len) {
		super(code, name, len, false);
		setWidth(-1); // 初始化宽度，在第一次取时根据选项明细重新计算
		_optCode = optCode;
	}

	@Override
	public boolean isOpt() {
		return true;
	}
	
	public T copyNew( String code, String name) {
		return (T)copy(new FldOptCust(code,name,_optCode,getLen()));
  }

	public OptBase getOpt() {
		return OptCustCtrl.getInstance().get(_optCode, false);
	}
	
	public OptBase getOpt(boolean isGet) {
		return OptCustCtrl.getInstance().get(_optCode, isGet);
	}

	@Override
	public String outTypeAndLen() {
		return "<用户选项：" + _optCode + "> " + super.outTypeAndLen();
	}

	@Override
	public String outSrcVarDefine() {
		IOptLine[] lines = getOpt().getLines();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < lines.length && i < 10; i++) {
			buf.append("	// " + lines[i].getKey() + ":" + lines[i].getName() + LN);
		}
		return super.outSrcVarDefine() + buf.toString();
	}

	@Override
	public VFld crtVFld() {
		return new VFldOptCust(this);
	}
	public String getOptCode(){
		return _optCode;
	}
}
