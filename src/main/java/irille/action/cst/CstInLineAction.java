package irille.action.cst;

import irille.action.ActionLineGoods;
import irille.pss.cst.CstInLine;

public class CstInLineAction extends ActionLineGoods<CstInLine> {

	public CstInLine getBean() {
		return _bean;
	}

	public void setBean(CstInLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return CstInLine.class;
	}
}
