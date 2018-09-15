package irille.action.pya;

import irille.action.ActionForm;
import irille.gl.pya.PyaPayWriteoffBill;

public class PyaPayWriteoffBillAction extends ActionForm<PyaPayWriteoffBill, PyaPayWriteoffBill> {

	@Override
	public Class beanClazz() {
		return PyaPayWriteoffBill.class;
	}

	public PyaPayWriteoffBill getBean() {
		return _bean;
	}

	public void setBean(PyaPayWriteoffBill bean) {
		this._bean = bean;
	}
	public static void getSubjects() throws Exception{
		PyaPayBillAction.getSubjects();
	}
}
