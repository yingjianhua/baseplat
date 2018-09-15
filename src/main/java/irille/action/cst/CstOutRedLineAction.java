package irille.action.cst;

import irille.action.ActionLineGoods;
import irille.pss.cst.CstOutRedLine;

public class CstOutRedLineAction extends ActionLineGoods<CstOutRedLine> {

	public CstOutRedLine getBean() {
		return _bean;
	}

	public void setBean(CstOutRedLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return CstOutRedLine.class;
	}
}
