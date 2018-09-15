package irille.action.sal;

import irille.action.ActionForm;
import irille.pss.sal.SalRtn;
import irille.pss.sal.SalRtnDAO;
import irille.pss.sal.SalRtnLine;

import java.util.List;

public class SalRtnAction extends ActionForm<SalRtn, SalRtnLine> {

	@Override
	public Class beanClazz() {
		return SalRtn.class;
	}

	public SalRtn getBean() {
		return _bean;
	}

	public void setBean(SalRtn bean) {
		this._bean = bean;
	}

	public List<SalRtnLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<SalRtnLine> listLine) {
		_listLine = listLine;
	}

	public void crtGs() throws Exception {
		SalRtnDAO.CrtGs act = new SalRtnDAO.CrtGs();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess(act.getB());
	}

}
