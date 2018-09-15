package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlGoodsLine;

public class GlGoodsLineAction extends ActionBase<GlGoodsLine> {
	public GlGoodsLine getBean() {
		return _bean;
	}

	public void setBean(GlGoodsLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlGoodsLine.class;
	}
}
