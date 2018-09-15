package irille.action.cst;

import irille.action.ActionLineGoods;
import irille.pss.cst.CstOutLine;

public class CstOutLineAction extends ActionLineGoods<CstOutLine> {

	public CstOutLine getBean() {
		return _bean;
	}

	public void setBean(CstOutLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return CstOutLine.class;
	}
}
