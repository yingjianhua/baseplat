package irille.action.cst;

import irille.action.ActionForm;
import irille.pss.cst.CstIn;
import irille.pss.cst.CstInDAO;
import irille.pss.cst.CstInLine;

import java.util.List;

public class CstInAction extends ActionForm<CstIn, CstInLine> {
	public String _formIds;

	@Override
	public Class beanClazz() {
		return CstIn.class;
	}

	public CstIn getBean() {
		return _bean;
	}

	public void setBean(CstIn bean) {
		this._bean = bean;
	}

	public List<CstInLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<CstInLine> listLine) {
		_listLine = listLine;
	}

	public String getFormIds() {
		return _formIds;
	}

	public void setFormIds(String formIds) {
		_formIds = formIds;
	}

	public CstIn insRun() throws Exception {
		CstInDAO.Ins ins = new CstInDAO.Ins();
		ins.setB(new CstIn());
		ins._formIds = getFormIds();
		ins.commit();
		return ins.getB();
	}
}
