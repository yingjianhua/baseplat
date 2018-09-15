package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlCarryOver;

public class GlCarryOverAction extends ActionBase<GlCarryOver> {

	@Override
	public Class beanClazz() {
		return GlCarryOver.class;
	}

	public GlCarryOver getBean() {
		return _bean;
	}

	public void setBean(GlCarryOver bean) {
		this._bean = bean;
	}

}
