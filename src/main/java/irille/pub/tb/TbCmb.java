//Created on 2005-9-17
package irille.pub.tb;

import irille.pub.Log;

/**
 * Title: 字段集<br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class TbCmb<THIS extends TbCmb> extends Tb<THIS> {
	private static final Log LOG = new Log(TbCmb.class);
	public TbCmb(Class classBean, String tbName) {
		super(classBean,tbName,tbName);
	}
	
	@Override
	public OBeanType getBeanType() {
		return OBeanType.CMB;
	}
}
