//Created on 2005-9-20
package irille.pub.bean;

import irille.core.sys.SysTableDAO;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import java.io.Serializable;


/**
 * Title: Long为主键的模型基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public abstract class BeanLong<BEAN extends BeanLong> extends BeanMain<BEAN,Long> {

	public static Tb TB=new Tb(BeanLong.class,"Long关键字的主表基类").setAutoIncrement();
	public static Fld FLD_PKEY=TB.crtLong("pkey","主键");
	private Long _pkey;

	@Override
	public Long getPkey() {
		return _pkey;
	}
	
	public void setPkey(Long pkey) {
		_pkey=pkey;
	}
	
	@Override
	public void setPkeyValues(Serializable... values) {
	  setPkey(Long.parseLong(values[0].toString()));
	}
	@Override
	public String outPkeyWhereSql() {
	  return " pkey="+_pkey;
	}
	@Override
	public void setPkeyByString(String pkeyValue) {
		 setPkey(Long.parseLong(pkeyValue));
	}

}
