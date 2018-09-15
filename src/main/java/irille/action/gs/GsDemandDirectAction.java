package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsDemandDirect;
import irille.pub.idu.Idu;

public class GsDemandDirectAction extends ActionBase<GsDemandDirect> {
	private boolean _isCreated = false;

	@Override
	public Class beanClazz() {
		return GsDemandDirect.class;
	}

	public GsDemandDirect getBean() {
		return _bean;
	}

	public void setBean(GsDemandDirect bean) {
		this._bean = bean;
	}

	//用户新增采购直销单或直销调入时
	public void listCrt() throws Exception {
		_isCreated = true;
		list();
	}

	public String crtAll() {
		String all = "1=1";
		if (_isCreated)
			all += " AND " + GsDemandDirect.T.ORG.getFld().getCodeSqlField() + " = " + Idu.getOrg().getPkey();
		return all;
	}
}
