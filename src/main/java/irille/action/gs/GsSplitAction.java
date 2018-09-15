package irille.action.gs;

import irille.action.ActionForm;
import irille.gl.gs.GsSplit;
import irille.gl.gs.GsSplitLine;

import java.util.List;

public class GsSplitAction extends ActionForm<GsSplit, GsSplitLine> {
	@Override
	public Class beanClazz() {
		return GsSplit.class;
	}

	public GsSplit getBean() {
		return _bean;
	}

	public void setBean(GsSplit bean) {
		this._bean = bean;
	}

	public List<GsSplitLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GsSplitLine> listLine) {
		_listLine = listLine;
	}
}
