//Created on 2005-9-20
package irille.pub.bean;

import java.io.Serializable;

import irille.pub.tb.Fld;
import irille.pub.tb.Tb;


/**
 * Title: Int为主键的模型基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public abstract class BeanInt<BEAN extends BeanInt> extends BeanMain<BEAN,Integer> {

	public static Tb TB=new Tb(BeanInt.class,"Integer关键字的主表基类").setAutoIncrement();
	public static Fld FLD_PKEY=TB.crtInt("pkey","主键");
	private Integer _pkey;

	@Override
	public Integer getPkey() {
		return _pkey;
	}
	
	public void setPkey(Integer pkey) {
		_pkey=pkey;
	}
	@Override
	public void setPkeyValues(Serializable... values) {
	  setPkey(Integer.parseInt(values[0].toString()));
	}
	@Override
	public String outPkeyWhereSql() {
	  return " pkey="+_pkey;
	}
	@Override
	public void setPkeyByString(String pkeyValue) {
		 setPkey(Integer.parseInt(pkeyValue));
	}
}
