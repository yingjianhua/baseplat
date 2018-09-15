//Created on 2005-9-20
package irille.pub.bean;

import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import java.io.Serializable;


public abstract class BeanStr<BEAN extends BeanStr> extends BeanMain<BEAN,String> {
	
	public static Tb TB=new Tb(BeanStr.class,"字符串关键字的主表基类");
	public static Fld FLD_PKEY=TB.crtStrPkey();
	private String _pkey;

	@Override
	public String getPkey() {
		return _pkey;
	}
	
	public void setPkey(String pkey) {
		_pkey=pkey;
	}
	@Override
	public void setPkeyValues(Serializable... values) {
	  setPkey((String)values[0]);
	}
	@Override
	public String outPkeyWhereSql() {
	  return " pkey='"+_pkey+",";
	}
	@Override
	public void setPkeyByString(String pkeyValue) {
		 setPkey(pkeyValue);
	}
}
