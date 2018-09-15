package irille.action.rva;

import irille.action.ActionForm;
import irille.gl.rva.RvaRecOtherWriteoffBill;

public class RvaRecOtherWriteoffBillAction extends ActionForm<RvaRecOtherWriteoffBill, RvaRecOtherWriteoffBill> {

	@Override
	public Class beanClazz() {
		return RvaRecOtherWriteoffBill.class;
	}

	public RvaRecOtherWriteoffBill getBean() {
		return _bean;
	}

	public void setBean(RvaRecOtherWriteoffBill bean) {
		this._bean = bean;
	}
	public static void getSubjects() throws Exception{
		RvaRecOtherBillAction.getSubjects();
	}
}
