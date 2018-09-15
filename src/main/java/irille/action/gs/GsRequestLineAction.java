package irille.action.gs;

import irille.action.ActionLineGoods;
import irille.gl.gs.GsRequestLine;

public class GsRequestLineAction extends ActionLineGoods<GsRequestLine> {

	public GsRequestLine getBean() {
		return _bean;
	}

	public void setBean(GsRequestLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsRequestLine.class;
	}
}
