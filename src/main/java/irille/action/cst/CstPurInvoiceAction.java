package irille.action.cst;

import irille.action.ActionForm;
import irille.pss.cst.CstPurInvoice;
import irille.pss.cst.CstPurInvoiceDAO;
import irille.pss.cst.CstPurInvoiceLine;

import java.util.List;

public class CstPurInvoiceAction extends ActionForm<CstPurInvoice, CstPurInvoiceLine>{
	public String _formIds;

	@Override
	public Class beanClazz() {
		return CstPurInvoice.class;
	}

	public CstPurInvoice getBean() {
		return _bean;
	}

	public void setBean(CstPurInvoice bean) {
		this._bean = bean;
	}

	public List<CstPurInvoiceLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<CstPurInvoiceLine> listLine) {
		_listLine = listLine;
	}

	public String getFormIds() {
		return _formIds;
	}

	public void setFormIds(String formIds) {
		_formIds = formIds;
	}

	public CstPurInvoice insRun() throws Exception {
		CstPurInvoiceDAO.Ins ins = new CstPurInvoiceDAO.Ins();
		ins.setB(new CstPurInvoice());
		ins._formIds = getFormIds();
		ins.commit();
		return ins.getB();
	}
}
