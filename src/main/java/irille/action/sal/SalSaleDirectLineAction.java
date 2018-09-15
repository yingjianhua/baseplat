package irille.action.sal;

import irille.action.ActionLineGoods;
import irille.pss.sal.SalSaleDirectLine;

public class SalSaleDirectLineAction extends ActionLineGoods<SalSaleDirectLine> {

	public SalSaleDirectLine getBean() {
		return _bean;
	}

	public void setBean(SalSaleDirectLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return SalSaleDirectLine.class;
	}
}
