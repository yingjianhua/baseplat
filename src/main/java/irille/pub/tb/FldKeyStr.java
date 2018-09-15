//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.tb.Infs.IFldKey;
import irille.pub.view.Fmts;
import irille.pub.view.VFld;
import irille.pub.view.VFldStr;


/**
 * Title: Long型的表主键字段<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class FldKeyStr<T extends FldKeyStr> extends FldStr<T> implements IFldKey<FldStr>{
	private FldStr _keyFld;
	/**
	 * 构造方法
	 */
	public FldKeyStr(FldStr keyFld, String code, String name) {
		super(code,name,keyFld.getLen(),false);
		_keyFld=keyFld;
		setFmt(Fmts.FMT_OUTKEY);
	}
	
	@SuppressWarnings("unchecked")
	@Override
  public T copyNew(String code, String name) {
		return (T)copy(new FldKeyStr(_keyFld,code,name));
  }

	public FldStr getOutKeyFld() {
	  return _keyFld;
  }
	@Override
  public VFld crtVFld() {
	  return new VFldStr(this);
  }
}
