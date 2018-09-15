package irille.action.frm;

import irille.action.ActionForm;
import irille.gl.frm.FrmHandover;
import irille.gl.frm.FrmHandoverLine;
import irille.gl.frm.FrmHandoverDAO.Take;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

public class FrmHandoverAction extends ActionForm<FrmHandover, FrmHandoverLine> {
	public FrmHandover getBean() {
		return _bean;
	}

	public void setBean(FrmHandover bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return FrmHandover.class;
	}
	
	public List<FrmHandoverLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<FrmHandoverLine> listLine) {
		_listLine = listLine;
	}
	
	public void take() throws Exception {
		Take act = new Take();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess(act.getB());
	}
	
	@Override
	public String crtAll() {
		return FrmHandover.T.CREATED_BY.getFld().getCodeSqlField() + "=" + getLoginSys().getPkey() 
				+ " OR " + FrmHandover.T.APPR_BY.getFld().getCodeSqlField() + "=" + getLoginSys().getPkey();
	}

}
