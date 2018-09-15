package irille.action.cst;

import irille.action.ActionForm;
import irille.pss.cst.CstSalInvoice;
import irille.pss.cst.CstSalInvoiceDAO;
import irille.pss.cst.CstSalInvoiceLine;
import irille.pss.sal.SalOrderDAO;

import java.util.List;

public class CstSalInvoiceAction extends ActionForm<CstSalInvoice, CstSalInvoiceLine> {
	public String _formIds;

	@Override
	public Class beanClazz() {
		return CstSalInvoice.class;
	}

	public CstSalInvoice getBean() {
		return _bean;
	}

	public void setBean(CstSalInvoice bean) {
		this._bean = bean;
	}

	public List<CstSalInvoiceLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<CstSalInvoiceLine> listLine) {
		_listLine = listLine;
	}

	public String getFormIds() {
		return _formIds;
	}

	public void setFormIds(String formIds) {
		_formIds = formIds;
	}

	public CstSalInvoice insRun() throws Exception {
		CstSalInvoiceDAO.Ins ins = new CstSalInvoiceDAO.Ins();
		ins.setB(new CstSalInvoice());
		ins._formIds = getFormIds();
		ins.commit();
		return ins.getB();
	}
}
