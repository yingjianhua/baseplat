//Created on 2005-12-29
package irille.pub.bean;

import java.math.BigDecimal;

/**
 * Title: 表单基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public abstract class BeanGoodsPrice<MAIN extends BeanGoodsPrice> extends BeanGoods<MAIN> implements IGoodsPrice {

	public BigDecimal getDefaultUomPrice() {
		return gtGoods().toDefaultPrice(gtUom(), getPrice());
	}

	public void recountPrice() {
		if (getQty().compareTo(ZERO) != 0)
			setPrice(getAmt().divide(getQty(), SYS.PRICE.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
	}

}
