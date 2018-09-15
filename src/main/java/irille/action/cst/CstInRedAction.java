package irille.action.cst;

import irille.action.ActionForm;
import irille.pss.cst.CstInRed;
import irille.pss.cst.CstInRedDAO;
import irille.pss.cst.CstInRedLine;

import java.util.List;

public class CstInRedAction extends ActionForm<CstInRed, CstInRedLine>{
	public String _formIds;

	@Override
	public Class beanClazz() {
		return CstInRed.class;
	}

	public CstInRed getBean() {
		return _bean;
	}

	public void setBean(CstInRed bean) {
		this._bean = bean;
	}

	public List<CstInRedLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<CstInRedLine> listLine) {
		_listLine = listLine;
	}

	public String getFormIds() {
		return _formIds;
	}

	public void setFormIds(String formIds) {
		_formIds = formIds;
	}

	public CstInRed insRun() throws Exception {
		CstInRedDAO.Ins ins = new CstInRedDAO.Ins();
		ins.setB(new CstInRed());
		ins._formIds = getFormIds();
		ins.commit();
		return ins.getB();
	}

}
