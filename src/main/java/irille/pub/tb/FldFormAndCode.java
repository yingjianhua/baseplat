//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.bean.IForm;


/**
 * Title: 本对象为FORM的主键字段，并会自动产生一个FORM对象的CODE字段<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class FldFormAndCode<T extends FldFormAndCode> extends FldLongTbObj<T> {
	private Fld _codeFld=null;
	/**
	 * 构造方法
	 */
	public FldFormAndCode(String code, String name) {
		super(code,name,IForm.class);
	}
	/* (non-Javadoc)
	 * @see irille.pub.tb.Fld#setTb(irille.pub.tb.TbBase)
	 */
	@Override
	public Fld setTb(TbBase tb) {
		super.setTb(tb);
		_codeFld = tb.add(new FldStr<FldStr>(getCode()+"Num",getName()+"号",40, isNull())).setAutoCreate().setNull(isNull());
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
  public T copyNew(String code, String name) {
		return (T)copy(new FldFormAndCode(code,name));
  }
	
	public Fld getCodeFld() {
		return _codeFld;
	}
}
