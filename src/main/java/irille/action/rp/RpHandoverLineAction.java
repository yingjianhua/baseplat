package irille.action.rp;

import irille.action.ActionLineTid;
import irille.gl.rp.RpHandoverLine;

public class RpHandoverLineAction extends ActionLineTid<RpHandoverLine> {

	public RpHandoverLine getBean() {
		return _bean;
	}

	public void setBean(RpHandoverLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return RpHandoverLine.class;
	}
}
