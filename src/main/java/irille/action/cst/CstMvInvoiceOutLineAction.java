package irille.action.cst;

import irille.action.ActionLineGoods;
import irille.pss.cst.CstMvInvoiceOutLine;

public class CstMvInvoiceOutLineAction extends ActionLineGoods<CstMvInvoiceOutLine> {

	public CstMvInvoiceOutLine getBean() {
		return _bean;
	}

	public void setBean(CstMvInvoiceOutLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return CstMvInvoiceOutLine.class;
	}
}
