package irille.action.rp;

import irille.action.ActionBase;
import irille.gl.rp.RpWorkBox;

public class RpWorkBoxAction extends ActionBase<RpWorkBox> {

	@Override
	public Class beanClazz() {
		return RpWorkBox.class;
	}

	public RpWorkBox getBean() {
		return _bean;
	}

	public void setBean(RpWorkBox bean) {
		this._bean = bean;
	}
	
}
