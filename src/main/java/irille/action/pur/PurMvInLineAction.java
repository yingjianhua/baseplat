package irille.action.pur;

import irille.action.ActionLineGoods;
import irille.pss.pur.PurMvInLine;

public class PurMvInLineAction extends ActionLineGoods<PurMvInLine> {

	public PurMvInLine getBean() {
		return _bean;
	}

	public void setBean(PurMvInLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return PurMvInLine.class;
	}
}
