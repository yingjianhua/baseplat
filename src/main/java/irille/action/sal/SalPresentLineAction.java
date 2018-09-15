package irille.action.sal;

import irille.action.ActionLineGoods;
import irille.pss.sal.SalPresentLine;

public class SalPresentLineAction extends ActionLineGoods<SalPresentLine> {

	public SalPresentLine getBean() {
		return _bean;
	}

	public void setBean(SalPresentLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return SalPresentLine.class;
	}
}
