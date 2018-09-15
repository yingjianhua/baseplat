package irille.action.gs;

import irille.action.ActionLineGoods;
import irille.gl.gs.GsMovementLine;
import irille.gl.gs.GsPhyinvGoodsLine;

public class GsPhyinvGoodsLineAction extends ActionLineGoods<GsPhyinvGoodsLine> {

	public GsPhyinvGoodsLine getBean() {
		return _bean;
	}

	public void setBean(GsPhyinvGoodsLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsPhyinvGoodsLine.class;
	}
}
