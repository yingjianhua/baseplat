package irille.action.cst;

import irille.action.ActionLineGoods;
import irille.pss.cst.CstPurInvoiceLine;

public class CstPurInvoiceLineAction extends ActionLineGoods<CstPurInvoiceLine>{
	public CstPurInvoiceLine getBean() {
		return _bean;
	}

	public void setBean(CstPurInvoiceLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return CstPurInvoiceLine.class;
	}
}
