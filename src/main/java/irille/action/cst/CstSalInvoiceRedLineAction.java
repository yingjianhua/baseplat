package irille.action.cst;

import irille.action.ActionLineGoods;
import irille.pss.cst.CstSalInvoiceRedLine;

public class CstSalInvoiceRedLineAction extends ActionLineGoods<CstSalInvoiceRedLine>{
	
	public CstSalInvoiceRedLine getBean() {
		return _bean;
	}

	public void setBean(CstSalInvoiceRedLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return CstSalInvoiceRedLine.class;
	}
	
}
