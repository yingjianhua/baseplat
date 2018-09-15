package irille.action.cst;

import irille.action.ActionLineGoods;
import irille.pss.cst.CstMvInvoiceInLine;

public class CstMvInvoiceInLineAction extends ActionLineGoods<CstMvInvoiceInLine> {

	public CstMvInvoiceInLine getBean() {
		return _bean;
	}

	public void setBean(CstMvInvoiceInLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return CstMvInvoiceInLine.class;
	}
}
