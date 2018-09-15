package irille.action.pur;

import irille.action.ActionLineGoods;
import irille.pss.pur.PurRevLine;

public class PurRevLineAction extends ActionLineGoods<PurRevLine> {

	public PurRevLine getBean() {
		return _bean;
	}

	public void setBean(PurRevLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return PurRevLine.class;
	}

}
