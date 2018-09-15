package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsStockLine;

public class GsStockLineAction extends ActionBase<GsStockLine> {
	
	public GsStockLine getBean() {
		return _bean;
	}

	public void setBean(GsStockLine bean) {
		this._bean = bean;
	}
	
	@Override
	public Class beanClazz() {
		return GsStockLine.class;
	}

}
