package irille.action.cst;

import irille.action.ActionForm;
import irille.pss.cst.CstMvInvoiceOut;
import irille.pss.cst.CstMvInvoiceOutDAO;
import irille.pss.cst.CstMvInvoiceOutLine;
import irille.pss.sal.SalOrderDAO;

import java.util.List;

public class CstMvInvoiceOutAction extends ActionForm<CstMvInvoiceOut, CstMvInvoiceOutLine> {
	public String _formIds;

	@Override
	public Class beanClazz() {
		return CstMvInvoiceOut.class;
	}

	public CstMvInvoiceOut getBean() {
		return _bean;
	}

	public void setBean(CstMvInvoiceOut bean) {
		this._bean = bean;
	}

	public List<CstMvInvoiceOutLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<CstMvInvoiceOutLine> listLine) {
		_listLine = listLine;
	}

	public String getFormIds() {
		return _formIds;
	}

	public void setFormIds(String formIds) {
		_formIds = formIds;
	}

	public CstMvInvoiceOut insRun() throws Exception {
		CstMvInvoiceOutDAO.Ins ins = new CstMvInvoiceOutDAO.Ins();
		ins.setB(new CstMvInvoiceOut());
		ins._formIds = getFormIds();
		ins.commit();
		return ins.getB();
	}
}
