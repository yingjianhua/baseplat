package irille.action.rva;

import irille.action.ActionForm;
import irille.gl.rva.RvaRecWriteoffBill;

public class RvaRecWriteoffBillAction extends ActionForm<RvaRecWriteoffBill, RvaRecWriteoffBill> {

	@Override
	public Class beanClazz() {
		return RvaRecWriteoffBill.class;
	}

	public RvaRecWriteoffBill getBean() {
		return _bean;
	}

	public void setBean(RvaRecWriteoffBill bean) {
		this._bean = bean;
	}
	public static void getSubjects() throws Exception{
		RvaRecBillAction.getSubjects();
	}
}
