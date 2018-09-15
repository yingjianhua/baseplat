package irille.action.sal;

import irille.action.ActionLineGoods;
import irille.pss.sal.SalRtnLine;

public class SalRtnLineAction extends ActionLineGoods<SalRtnLine> {

	public SalRtnLine getBean() {
		return _bean;
	}

	public void setBean(SalRtnLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return SalRtnLine.class;
	}
}
