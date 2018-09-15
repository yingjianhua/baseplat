package irille.action.rva;

import irille.action.ActionForm;
import irille.gl.rva.RvaRecDepWriteoffBill;

public class RvaRecDepWriteoffBillAction extends ActionForm<RvaRecDepWriteoffBill, RvaRecDepWriteoffBill> {

	@Override
	public Class beanClazz() {
		return RvaRecDepWriteoffBill.class;
	}

	public RvaRecDepWriteoffBill getBean() {
		return _bean;
	}

	public void setBean(RvaRecDepWriteoffBill bean) {
		this._bean = bean;
	}
	public static void getSubjects() throws Exception{
		RvaRecDepBillAction.getSubjects();
	}
}
