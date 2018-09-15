package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlEntryDef;

import java.util.List;

public class GlEntryDefAction extends ActionBase<GlEntryDef> {
	
	public GlEntryDef getBean() {
		return _bean;
	}

	public void setBean(GlEntryDef bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlEntryDef.class;
	}

}
