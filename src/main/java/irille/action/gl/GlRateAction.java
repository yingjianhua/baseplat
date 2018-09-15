package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlRate;
import irille.pub.svr.Env;

public class GlRateAction extends ActionBase<GlRate> {
	public GlRate getBean() {
		return _bean;
	}

	public void setBean(GlRate bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlRate.class;
	}
	
	@Override
	public void insBefore() {
	  super.insBefore();
	  _bean.setCreatedBy(Env.INST.getTran().getUser().getPkey());
	  _bean.setCreatedTime(Env.INST.getWorkDate());
	}
}
