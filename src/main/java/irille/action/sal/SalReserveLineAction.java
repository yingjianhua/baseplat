package irille.action.sal;

import irille.action.ActionLineGoods;
import irille.pss.sal.SalReserveLine;

public class SalReserveLineAction extends ActionLineGoods<SalReserveLine> {

	public SalReserveLine getBean() {
		return _bean;
	}

	public void setBean(SalReserveLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return SalReserveLine.class;
	}
}
