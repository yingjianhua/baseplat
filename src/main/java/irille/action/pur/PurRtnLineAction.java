package irille.action.pur;

import irille.action.ActionLineGoods;
import irille.pss.pur.PurRtnLine;

public class PurRtnLineAction extends ActionLineGoods<PurRtnLine> {

	public PurRtnLine getBean() {
		return _bean;
	}

	public void setBean(PurRtnLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return PurRtnLine.class;
	}

}
