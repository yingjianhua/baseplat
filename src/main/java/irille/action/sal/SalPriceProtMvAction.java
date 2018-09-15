package irille.action.sal;

import irille.action.ActionBase;
import irille.pss.sal.SalPriceProtMv;

public class SalPriceProtMvAction extends ActionBase<SalPriceProtMv> {
	public SalPriceProtMv getBean() {
		return _bean;
	}

	public void setBean(SalPriceProtMv bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return SalPriceProtMv.class;
	}

}
