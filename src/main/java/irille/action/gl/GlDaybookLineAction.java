package irille.action.gl;

import irille.action.ActionLineTid;
import irille.gl.gl.GlDaybookLine;

public class GlDaybookLineAction extends ActionLineTid<GlDaybookLine> {
	public GlDaybookLine getBean() {
		return _bean;
	}

	public void setBean(GlDaybookLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlDaybookLine.class;
	}
}
