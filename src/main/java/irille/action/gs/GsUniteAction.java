package irille.action.gs;

import irille.action.ActionForm;
import irille.gl.gs.GsUnite;
import irille.gl.gs.GsUniteLine;

import java.util.List;

public class GsUniteAction extends ActionForm<GsUnite, GsUniteLine> {

	@Override
	public Class beanClazz() {
		return GsUnite.class;
	}

	public GsUnite getBean() {
		return _bean;
	}

	public void setBean(GsUnite bean) {
		this._bean = bean;
	}

	public List<GsUniteLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GsUniteLine> listLine) {
		_listLine = listLine;
	}
}
