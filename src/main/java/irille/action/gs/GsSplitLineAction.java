package irille.action.gs;

import irille.action.ActionLineGoods;
import irille.gl.gs.GsSplitLine;

public class GsSplitLineAction extends ActionLineGoods<GsSplitLine> {
	public GsSplitLine getBean() {
		return _bean;
	}

	public void setBean(GsSplitLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsSplitLine.class;
	}

}
