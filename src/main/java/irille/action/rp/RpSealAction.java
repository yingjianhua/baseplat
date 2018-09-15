package irille.action.rp;

import irille.action.ActionBase;
import irille.gl.rp.RpSeal;

public class RpSealAction extends ActionBase<RpSeal> {

	@Override
	public Class beanClazz() {
		return RpSeal.class;
	}

	public RpSeal getBean() {
		return _bean;
	}

	public void setBean(RpSeal bean) {
		this._bean = bean;
	}

}
