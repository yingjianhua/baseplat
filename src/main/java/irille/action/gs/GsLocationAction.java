package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsLocation;

public class GsLocationAction extends ActionBase<GsLocation> {
	public GsLocation getBean() {
		return _bean;
	}

	public void setBean(GsLocation bean) {
		this._bean = bean;
	}
	
	@Override
	public Class beanClazz() {
		return GsLocation.class;
	}

}
