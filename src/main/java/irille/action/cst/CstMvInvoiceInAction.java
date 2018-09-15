package irille.action.cst;

import irille.action.ActionForm;
import irille.pss.cst.CstMvInvoiceIn;
import irille.pss.cst.CstMvInvoiceInDAO;
import irille.pss.cst.CstMvInvoiceInLine;

import java.util.List;

public class CstMvInvoiceInAction extends ActionForm<CstMvInvoiceIn, CstMvInvoiceInLine> {
	public String _formIds;

	@Override
	public Class beanClazz() {
		return CstMvInvoiceIn.class;
	}

	public CstMvInvoiceIn getBean() {
		return _bean;
	}

	public void setBean(CstMvInvoiceIn bean) {
		this._bean = bean;
	}

	public List<CstMvInvoiceInLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<CstMvInvoiceInLine> listLine) {
		_listLine = listLine;
	}

	public String getFormIds() {
		return _formIds;
	}

	public void setFormIds(String formIds) {
		_formIds = formIds;
	}

	public CstMvInvoiceIn insRun() throws Exception {
		CstMvInvoiceInDAO.Ins ins = new CstMvInvoiceInDAO.Ins();
		ins.setB(new CstMvInvoiceIn());
		ins._formIds = getFormIds();
		ins.commit();
		return ins.getB();
	}
}
