package irille.action.sal;

import irille.action.ActionLineGoods;
import irille.pss.sal.SalSaleLine;

public class SalSaleLineAction extends ActionLineGoods<SalSaleLine> {

	public SalSaleLine getBean() {
		return _bean;
	}

	public void setBean(SalSaleLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return SalSaleLine.class;
	}

}
