package irille.action.lgt;

import irille.action.ActionBase;
import irille.pss.lgt.LgtShipMode;

public class LgtShipModeAction extends ActionBase<LgtShipMode> {

	@Override
	public Class beanClazz() {
		return LgtShipMode.class;
	}
	
	public LgtShipMode getBean() {
		return _bean;
	}

	public void setBean(LgtShipMode bean) {
		this._bean = bean;
	}
}
