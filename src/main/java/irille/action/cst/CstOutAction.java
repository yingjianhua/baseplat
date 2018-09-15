package irille.action.cst;

import irille.action.ActionForm;
import irille.pss.cst.CstOut;
import irille.pss.cst.CstOutDAO;
import irille.pss.cst.CstOutLine;

import java.util.List;

public class CstOutAction extends ActionForm<CstOut, CstOutLine> {
	public String _formIds;

	@Override
	public Class beanClazz() {
		return CstOut.class;
	}

	public CstOut getBean() {
		return _bean;
	}

	public void setBean(CstOut bean) {
		this._bean = bean;
	}

	public List<CstOutLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<CstOutLine> listLine) {
		_listLine = listLine;
	}

	public String getFormIds() {
		return _formIds;
	}

	public void setFormIds(String formIds) {
		_formIds = formIds;
	}

	public CstOut insRun() throws Exception {
		CstOutDAO.Ins ins = new CstOutDAO.Ins();
		ins.setB(new CstOut());
		ins._formIds = getFormIds();
		ins.commit();
		return ins.getB();
	}
}
