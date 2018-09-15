package irille.action.gs;

import irille.action.ActionLineGoods;
import irille.gl.gs.GsGainLine;

public class GsGainLineAction extends ActionLineGoods<GsGainLine> {

	public GsGainLine getBean() {
		return _bean;
	}

	public void setBean(GsGainLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsGainLine.class;
	}

}
