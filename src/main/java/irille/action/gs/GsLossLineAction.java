package irille.action.gs;

import irille.action.ActionLineGoods;
import irille.gl.gs.GsGainLine;
import irille.gl.gs.GsLossLine;

public class GsLossLineAction extends ActionLineGoods<GsLossLine> {
	
	public GsLossLine getBean() {
		return _bean;
	}

	public void setBean(GsLossLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsLossLine.class;
	}

}
