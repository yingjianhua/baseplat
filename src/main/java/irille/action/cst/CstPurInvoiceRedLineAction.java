package irille.action.cst;

import irille.action.ActionLineGoods;
import irille.pss.cst.CstPurInvoiceRedLine;

public class CstPurInvoiceRedLineAction extends ActionLineGoods<CstPurInvoiceRedLine>{
	
	public CstPurInvoiceRedLine getBean() {
		return _bean;
	}

	public void setBean(CstPurInvoiceRedLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return CstPurInvoiceRedLine.class;
	}
	
}
