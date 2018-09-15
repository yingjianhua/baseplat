package irille.action.cst;

import irille.action.ActionForm;
import irille.pss.cst.CstSalInvoiceRed;
import irille.pss.cst.CstSalInvoiceRedDAO;
import irille.pss.cst.CstSalInvoiceRedLine;

import java.util.List;

public class CstSalInvoiceRedAction extends ActionForm<CstSalInvoiceRed, CstSalInvoiceRedLine>{
	public String _formIds;

	@Override
	public Class beanClazz() {
		return CstSalInvoiceRed.class;
	}

	public CstSalInvoiceRed getBean() {
		return _bean;
	}

	public void setBean(CstSalInvoiceRed bean) {
		this._bean = bean;
	}

	public List<CstSalInvoiceRedLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<CstSalInvoiceRedLine> listLine) {
		_listLine = listLine;
	}

	public String getFormIds() {
		return _formIds;
	}

	public void setFormIds(String formIds) {
		_formIds = formIds;
	}

	public CstSalInvoiceRed insRun() throws Exception {
		CstSalInvoiceRedDAO.Ins ins = new CstSalInvoiceRedDAO.Ins();
		ins.setB(new CstSalInvoiceRed());
		ins._formIds = getFormIds();
		ins.commit();
		return ins.getB();
	}

}
