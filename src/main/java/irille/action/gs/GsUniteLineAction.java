package irille.action.gs;

import irille.action.ActionLineGoods;
import irille.gl.gs.GsUniteLine;

public class GsUniteLineAction extends ActionLineGoods<GsUniteLine> {
	public GsUniteLine getBean() {
		return _bean;
	}

	public void setBean(GsUniteLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsUniteLine.class;
	}

}
