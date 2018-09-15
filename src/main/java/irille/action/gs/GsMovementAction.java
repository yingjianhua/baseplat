package irille.action.gs;

import irille.action.ActionForm;
import irille.gl.gs.GsMovement;
import irille.gl.gs.GsMovementLine;

import java.util.List;

public class GsMovementAction extends ActionForm<GsMovement, GsMovementLine> {

	@Override
	public Class beanClazz() {
		return GsMovement.class;
	}

	public GsMovement getBean() {
		return _bean;
	}

	public void setBean(GsMovement bean) {
		this._bean = bean;
	}

	public List<GsMovementLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GsMovementLine> listLine) {
		_listLine = listLine;
	}

}
