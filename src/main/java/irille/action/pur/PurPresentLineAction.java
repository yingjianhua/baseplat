package irille.action.pur;

import irille.action.ActionLineGoods;
import irille.pss.pur.PurPresentLine;

public class PurPresentLineAction extends ActionLineGoods<PurPresentLine> {

	public PurPresentLine getBean() {
		return _bean;
	}

	public void setBean(PurPresentLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return PurPresentLine.class;
	}

}
