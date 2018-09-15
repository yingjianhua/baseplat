package irille.action.cst;

import irille.action.ActionForm;
import irille.pss.cst.CstOutRed;
import irille.pss.cst.CstOutRedDAO;
import irille.pss.cst.CstOutRedLine;

import java.util.List;

public class CstOutRedAction extends ActionForm<CstOutRed, CstOutRedLine> {
	public String _formIds;

	@Override
	public Class beanClazz() {
		return CstOutRed.class;
	}

	public CstOutRed getBean() {
		return _bean;
	}

	public void setBean(CstOutRed bean) {
		this._bean = bean;
	}

	public List<CstOutRedLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<CstOutRedLine> listLine) {
		_listLine = listLine;
	}

	public String getFormIds() {
		return _formIds;
	}

	public void setFormIds(String formIds) {
		_formIds = formIds;
	}

	public CstOutRed insRun() throws Exception {
		CstOutRedDAO.Ins ins = new CstOutRedDAO.Ins();
		ins.setB(new CstOutRed());
		ins._formIds = getFormIds();
		ins.commit();
		return ins.getB();
	}
}
