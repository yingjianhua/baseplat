package irille.action.cst;

import irille.action.ActionForm;
import irille.pss.cst.CstPurInvoiceRed;
import irille.pss.cst.CstPurInvoiceRedDAO;
import irille.pss.cst.CstPurInvoiceRedLine;

import java.util.List;

public class CstPurInvoiceRedAction extends ActionForm<CstPurInvoiceRed, CstPurInvoiceRedLine> {
	public String _formIds;

	@Override
	public Class beanClazz() {
		return CstPurInvoiceRed.class;
	}

	public CstPurInvoiceRed getBean() {
		return _bean;
	}

	public void setBean(CstPurInvoiceRed bean) {
		this._bean = bean;
	}

	public List<CstPurInvoiceRedLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<CstPurInvoiceRedLine> listLine) {
		_listLine = listLine;
	}

	public String getFormIds() {
		return _formIds;
	}

	public void setFormIds(String formIds) {
		_formIds = formIds;
	}

	public CstPurInvoiceRed insRun() throws Exception {
		CstPurInvoiceRedDAO.Ins ins = new CstPurInvoiceRedDAO.Ins();
		ins.setB(new CstPurInvoiceRed());
		ins._formIds = getFormIds();
		ins.commit();
		return ins.getB();
	}
}
