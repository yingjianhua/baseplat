package irille.action.frm;

import irille.action.ActionBase;
import irille.gl.frm.FrmLink;

public class FrmLinkAction extends ActionBase<FrmLink> {
	public FrmLink getBean() {
		return _bean;
	}

	public void setBean(FrmLink bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return FrmLink.class;
	}

}
