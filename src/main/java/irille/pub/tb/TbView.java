//Created on 2005-9-17
package irille.pub.tb;

import irille.pub.Log;

import java.util.Vector;

/**
 * Title: 表视图<br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class TbView<THIS extends TbView> extends Tb<THIS> {
	private static final Log LOG = new Log(TbView.class);
	private Vector<TbBase> _tbs=new Vector();
	private StringBuilder _buf =new StringBuilder();
	
	public TbView(Class classBean, String tbName) {
		super(classBean,tbName,tbName);
	}
	public TbView(Class classBean, String tbName,String shortName) {
		super(classBean,tbName,shortName);
	}
	
	public TbView addSelectFld(IEnumFld... fromFlds) {
		for(IEnumFld fld: fromFlds)
			_buf.append(","+getTbShortName(fld.getFld().getTb())+"."+fld.getFld().getCodeSqlField());
		return (THIS)this;
	}
	
	public TbView addSelectExp(String exp) {
			_buf.append(","+exp);
		return (THIS)this;
	}
	
	public String getSelectFlds() {
		return _buf.toString().substring(1);
	}
	
	public String getTbShortName(TbBase tb) {
		int i=0;
		for(TbBase t:_tbs) {
			i++;
			if(t==tb)
				return ""+(char)('a'+i);
		}
		_tbs.add(tb);
		return ""+(char)('a'+i);
	}
	
	@Override
	public OBeanType getBeanType() {
		return OBeanType.VIEW;
	}

}
