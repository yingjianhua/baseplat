package irille.action.gl;

import irille.action.ActionForm;
import irille.gl.gl.GlTransform;

public class GlTransformAction extends ActionForm<GlTransform, GlTransform> {
	@Override
	public Class beanClazz() {
		return GlTransform.class;
	}

	public GlTransform getBean() {
		return _bean;
	}

	public void setBean(GlTransform bean) {
		this._bean = bean;
	}

}
