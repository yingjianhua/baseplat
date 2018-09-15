package irille.action.gs;

import irille.action.ActionLineGoods;
import irille.gl.gs.GsPhyinvBatchLine;

public class GsPhyinvBatchLineAction extends ActionLineGoods<GsPhyinvBatchLine> {

	public GsPhyinvBatchLine getBean() {
		return _bean;
	}

	public void setBean(GsPhyinvBatchLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsPhyinvBatchLine.class;
	}
	
}
