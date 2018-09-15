//Created on 2005-12-29
package irille.pub.bean;

import irille.gl.gs.GsGoods;
import irille.gl.gs.GsInDAO;
import irille.gl.gs.GsOutLineView;
import irille.pub.Log;
import irille.pub.tb.Tb;
import irille.pub.tb.TbView;

import java.math.BigDecimal;

/**
 * Title: 表单基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public abstract class BeanGoods<MAIN extends BeanGoods> extends BeanLong<MAIN> implements IGoods {
	public static final Log LOG = new Log(BeanGoods.class);
	
	public BigDecimal getDefaultUomQty() {
		return gtGoods().toDefaultQty(gtUom(), getQty());
	}
	
	public void checkUomType() {
		GsGoods goods = gtGoods();
		if (gtUom().getUomType().intValue() != goods.gtUom().getUomType().intValue())
			throw LOG.err("uomType", GsGoods.TB.getShortName() + "[{0} : {1}]单位不一致!", goods.getCode(),goods.getName());
	}

}
