package irille.action.prv;

import irille.action.ActionBase;
import irille.core.prv.PrvRole;
import irille.core.prv.PrvRoleDAO;
import irille.core.sys.Sys;

import java.io.IOException;

import org.json.JSONException;

public class PrvRoleAction extends ActionBase<PrvRole> {
	public String _lines;
	public String _type;

	@Override
	public Class beanClazz() {
		return PrvRole.class;
	}

	public PrvRole getBean() {
		return _bean;
	}

	public void setBean(PrvRole bean) {
		this._bean = bean;
	}

	public String getLines() {
		return _lines;
	}

	public void setLines(String lines) {
		_lines = lines;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}

	@Override
	public String crtAll() {
		return PrvRole.T.ENABLED.getFld().getCode() + "=" + Sys.OEnabled.TRUE.getLine().getKey();
	}

	@Override
	public String orderBy() {
		return " ORDER BY " + PrvRole.T.CODE;
	}

	public void updCtrl() throws IOException, JSONException {
		PrvRoleDAO.UpdCtrl act = new PrvRoleDAO.UpdCtrl();
		act.setBKey(getPkey());
		act.setLines(getLines());
		act.setType(getType());
		act.commit();
		writeSuccess();
	}

}
