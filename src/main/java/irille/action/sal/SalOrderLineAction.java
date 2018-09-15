package irille.action.sal;

import irille.action.ActionLineGoods;
import irille.pss.sal.SalOrderLine;

public class SalOrderLineAction extends ActionLineGoods<SalOrderLine> {

	public SalOrderLine getBean() {
		return _bean;
	}

	public void setBean(SalOrderLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return SalOrderLine.class;
	}
}
