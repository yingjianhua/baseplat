package irille.action.gs;

import irille.action.ActionLineGoods;
import irille.gl.gs.GsMovementLine;

public class GsMovementLineAction extends ActionLineGoods<GsMovementLine> {

	public GsMovementLine getBean() {
		return _bean;
	}

	public void setBean(GsMovementLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsMovementLine.class;
	}
}
