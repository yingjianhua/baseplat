package irille.action.cst;

import irille.action.ActionLineGoods;
import irille.pss.cst.CstSalInvoiceLine;
import irille.pss.sal.SalReserveLine;

public class CstSalInvoiceLineAction extends ActionLineGoods<CstSalInvoiceLine> {

	public CstSalInvoiceLine getBean() {
		return _bean;
	}

	public void setBean(CstSalInvoiceLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return CstSalInvoiceLine.class;
	}
}
