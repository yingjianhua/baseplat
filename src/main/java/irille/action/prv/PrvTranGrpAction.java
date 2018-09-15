package irille.action.prv;

import irille.action.ActionBase;
import irille.core.prv.PrvTranGrp;

public class PrvTranGrpAction extends ActionBase<PrvTranGrp> {

	@Override
	public Class beanClazz() {
		return PrvTranGrp.class;
	}

	public PrvTranGrp getBean() {
		return _bean;
	}

	public void setBean(PrvTranGrp bean) {
		this._bean = bean;
	}

}
