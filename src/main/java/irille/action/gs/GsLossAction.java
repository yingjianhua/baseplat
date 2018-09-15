package irille.action.gs;

import irille.action.ActionForm;
import irille.gl.gs.GsLoss;
import irille.gl.gs.GsLossLine;

import java.util.List;

public class GsLossAction extends ActionForm<GsLoss, GsLossLine> {
	@Override
	public Class beanClazz() {
		return GsLoss.class;
	}

	public GsLoss getBean() {
		return _bean;
	}

	public void setBean(GsLoss bean) {
		this._bean = bean;
	}

	public List<GsLossLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GsLossLine> listLine) {
		_listLine = listLine;
	}

}
