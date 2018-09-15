package irille.action.pya;

import irille.action.ActionForm;
import irille.gl.pya.PyaPayOtherWriteoffBill;

public class PyaPayOtherWriteoffBillAction extends ActionForm<PyaPayOtherWriteoffBill, PyaPayOtherWriteoffBill> {

	@Override
	public Class beanClazz() {
		return PyaPayOtherWriteoffBill.class;
	}

	public PyaPayOtherWriteoffBill getBean() {
		return _bean;
	}

	public void setBean(PyaPayOtherWriteoffBill bean) {
		this._bean = bean;
	}
	public static void getSubjects() throws Exception{
		PyaPayOtherBillAction.getSubjects();
	}
}
