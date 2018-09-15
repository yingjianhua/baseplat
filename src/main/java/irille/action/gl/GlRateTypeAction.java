package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlRateType;
import irille.pub.svr.Env;

public class GlRateTypeAction extends ActionBase<GlRateType> {
	public GlRateType getBean() {
		return _bean;
	}

	public void setBean(GlRateType bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlRateType.class;
	}
	
	@Override
	public void insBefore() {
	  super.insBefore();
	  _bean.setCreatedBy(Env.INST.getTran().getUser().getPkey());
	  _bean.setCreatedTime(Env.INST.getWorkDate());
	}
}
