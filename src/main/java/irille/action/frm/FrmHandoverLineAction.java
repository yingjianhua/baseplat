package irille.action.frm;

import irille.action.ActionBase;
import irille.action.ActionLineTid;
import irille.gl.frm.FrmHandoverLine;

public class FrmHandoverLineAction extends ActionLineTid<FrmHandoverLine> {
	public FrmHandoverLine getBean() {
		return _bean;
	}

	public void setBean(FrmHandoverLine bean) {
		this._bean = bean;
	}
	
	@Override
	public Class beanClazz() {
		return FrmHandoverLine.class;
	}
}