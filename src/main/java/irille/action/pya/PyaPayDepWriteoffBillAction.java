package irille.action.pya;

import irille.action.ActionForm;
import irille.gl.pya.PyaPayDepWriteoffBill;

public class PyaPayDepWriteoffBillAction extends ActionForm<PyaPayDepWriteoffBill, PyaPayDepWriteoffBill> {

	@Override
	public Class beanClazz() {
		return PyaPayDepWriteoffBill.class;
	}

	public PyaPayDepWriteoffBill getBean() {
		return _bean;
	}

	public void setBean(PyaPayDepWriteoffBill bean) {
		this._bean = bean;
	}
	public static void getSubjects() throws Exception{
		PyaPayDepBillAction.getSubjects();
	}
}
