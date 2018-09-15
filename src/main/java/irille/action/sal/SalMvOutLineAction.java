package irille.action.sal;

import irille.action.ActionLineGoods;
import irille.pss.sal.SalMvOutLine;

public class SalMvOutLineAction extends ActionLineGoods<SalMvOutLine> {

	public SalMvOutLine getBean() {
		return _bean;
	}

	public void setBean(SalMvOutLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return SalMvOutLine.class;
	}
}
