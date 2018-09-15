package irille.action.rp;

import irille.action.ActionForm;
import irille.gl.rp.RpHandover;
import irille.gl.rp.RpHandoverLine;

import java.util.List;

public class RpHandoverAction extends ActionForm<RpHandover,RpHandoverLine> {

	@Override
	public Class beanClazz() {
		return RpHandover.class;
	}

	public RpHandover getBean() {
		return _bean;
	}

	public void setBean(RpHandover bean) {
		this._bean = bean;
	}

	public List<RpHandoverLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<RpHandoverLine> listLine) {
		_listLine = listLine;
	}
}
