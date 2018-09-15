package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlDailyLedger;

public class GlDailyLedgerAction extends ActionBase<GlDailyLedger> {
	public GlDailyLedger getBean() {
		return _bean;
	}

	public void setBean(GlDailyLedger bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlDailyLedger.class;
	}
}
