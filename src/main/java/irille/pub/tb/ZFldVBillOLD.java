//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.bean.BeanBill;

/**
 * Title: 组合的凭证虚拟字段<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class ZFldVBillOLD<T extends ZFldVBillOLD> extends ZFldVFormOLD<T> {
	public ZFldVBillOLD(String code, String name,Fld tbIdFld,Fld tbPkeyFld) {
		super(code, name,tbIdFld,tbPkeyFld);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T)copy( new ZFldVBillOLD( code, name,getTbIdFld(),getTbPkeyFld() ));
	}
	
	@Override
	public Class getJavaType() {
		return BeanBill.class;
	}
}
