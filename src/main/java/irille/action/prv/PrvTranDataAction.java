package irille.action.prv;

import irille.action.ActionBase;
import irille.core.prv.PrvTranData;

public class PrvTranDataAction extends ActionBase<PrvTranData> {

	@Override
	public Class beanClazz() {
		return PrvTranData.class;
	}

	public PrvTranData getBean() {
		return _bean;
	}

	public void setBean(PrvTranData bean) {
		this._bean = bean;
	}

}
