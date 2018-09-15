package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlSubject;

public class GlSubjectAction extends ActionBase<GlSubject> {

	@Override
	public Class beanClazz() {
		return GlSubject.class;
	}

	public GlSubject getBean() {
		return _bean;
	}

	public void setBean(GlSubject bean) {
		this._bean = bean;
	}
	
	@Override
	public String orderBy() {
	  return " ORDER BY CODE";
	}

}
