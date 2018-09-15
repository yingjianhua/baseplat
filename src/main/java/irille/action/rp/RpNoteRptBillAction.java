package irille.action.rp;

import irille.action.ActionForm;
import irille.gl.rp.RpNoteRptBill;

public class RpNoteRptBillAction extends ActionForm<RpNoteRptBill,RpNoteRptBill>{
	@Override
	public Class beanClazz() {
	  return RpNoteRptBill.class;
	}
	
	public RpNoteRptBill getBean() {
		return _bean;
	}

	public void setBean(RpNoteRptBill bean) {
		this._bean = bean;
	}
	
}
