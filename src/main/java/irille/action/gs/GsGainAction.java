package irille.action.gs;

import irille.action.ActionForm;
import irille.gl.gs.GsGain;
import irille.gl.gs.GsGainLine;

import java.util.List;

public class GsGainAction extends ActionForm<GsGain, GsGainLine> {
	@Override
	public Class beanClazz() {
		return GsGain.class;
	}

	public GsGain getBean() {
		return _bean;
	}

	public void setBean(GsGain bean) {
		this._bean = bean;
	}

	public List<GsGainLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GsGainLine> listLine) {
		_listLine = listLine;
	}

}
