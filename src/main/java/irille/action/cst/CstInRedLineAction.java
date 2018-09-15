package irille.action.cst;

import irille.action.ActionLineGoods;
import irille.pss.cst.CstInRedLine;

public class CstInRedLineAction extends ActionLineGoods<CstInRedLine>{
	
	public CstInRedLine getBean() {
		return _bean;
	}

	public void setBean(CstInRedLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return CstInRedLine.class;
	}
	
}
